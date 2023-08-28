package com.SerenityBDDForGemini.support;

import com.SerenityBDDForGemini.execute.Perform;
import net.thucydides.core.annotations.Steps;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class GridObjectOperations extends PageObjectOperations {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(GridObjectOperations.class).atInfo();

    @Steps
    Perform perform;

    @Steps
    PageObjectOperations pageObjectOperations;

    @Steps
    DataObjectOperations dataObjectOperations;

    /**
     * Returns all Table Headers including the empty headings
     * @param table: {@link By}
     * @return List<String>
     */
    public List<String> getAllTableHeaders(By table) {
        WebElement tableElement = perform.getWebElement(table);
        return getAllTableHeaders(tableElement);
    }

    /**
     * Returns all Table Headers including the empty headings
     * @param tableElement: {@link WebElement}
     * @return List<String>
     */
    public List<String> getAllTableHeaders(WebElement tableElement) {
        return tableElement.findElements(By.tagName("th")).stream().map(WebElement::getText)
                .toList().stream().map(String::trim).toList();
    }

    /**
     * Returns the Visible Table Headers
     * @param table
     * @return List<String>
     */
    public List<String> getTableHeaders(By table) {
        return getAllTableHeaders(table).stream().filter(r -> !r.equals("")).toList();
    }

    /**
     * Returns the Visible Table Headers
     * @param tableElement: {@link WebElement}
     * @return List<String>
     */
    public List<String> getTableHeaders(WebElement tableElement) {
        return getAllTableHeaders(tableElement).stream().filter(r -> !r.equals("")).toList();
    }

    public HashMap<String, ArrayList<String>> getRowsDataForColumns(List<String> columns, By table) {
        HashMap<String, ArrayList<String>> rowsForColumnsMap = new HashMap<>();
        List<String> tableHeaders = getAllTableHeaders(table);
        WebElement tableElement = perform.getWebElement(table);
        for (String column : columns) {
            int colIndex = tableHeaders.indexOf(column);
            List<WebElement> rowElements = tableElement.findElements(By.cssSelector("tbody tr"));
            rowsForColumnsMap.put(column, new ArrayList<>(){{
                for (WebElement rowElement : rowElements) {
                    add(rowElement.findElement(By.cssSelector("td:nth-child(" + (colIndex + 1) + ")")).getText());
                }
            }});
        }
        return rowsForColumnsMap;
    }

    public Map<String, String> getValueForRowColumn(Map<String, String> tableRow, By table) {
        return getValueForRowColumn(tableRow, getDriver().findElement(table));
    }

    public Map<String, String> getValueForRowColumn(Map<String, String> tableRow, WebElement table) {
        Map<String, String> results = new HashMap<>();
        List<String> columns = new ArrayList<>(tableRow.keySet());
        if (columns.indexOf("row") != 0) {
            int tempIndex = columns.indexOf("row");
            columns.set(tempIndex, columns.get(0));
            columns.set(0, "row");
        }
        results.put("row", tableRow.get("row"));
        for (int i = 1; i < columns.size(); i++) {
            int rowNumber = Integer.parseInt(tableRow.get("row")) - 1;
            LOGGER_INFO.log("Getting Data for: " + columns.get(i));
            String tdSelector = "td:nth-child(" + (getAllTableHeaders(table).indexOf(columns.get(i)) + 1) + ")";
            String result = table.findElements(By.cssSelector("tbody tr")).get(rowNumber).findElement(
                    By.cssSelector(tdSelector)).getText();
            if (Objects.equals(result, "")) {
                result = null;
            }
            results.put(columns.get(i), result);
        }
        return results;
    }

    public HashMap<String, String> getRowFocusedLocator(int rowNumber, String gridClassName) {
        Field elementLoaded = pageObjectOperations.poeFieldClass("GRID_LOADED", gridClassName);
        Class<?> gridClass = pageObjectOperations.getPageClass(gridClassName);
        By field = perform.fieldToInteract(elementLoaded, gridClass);
        WebElement table = perform.getWebElement(elementLoaded, gridClass);
        if (field.toString().toLowerCase().contains("xpath")) {
            return constructFieldSelector("table", field, "/tbody/tr[" + rowNumber + "]");
        } else {
            return constructFieldSelector("table", field, ">tbody tr:nth-child(" + rowNumber + ")");
        }
    }

    public WebElement getRowFocusedWebElement(int rowNumber, String gridPage) {
        HashMap<String, String> rowElementMap = getRowFocusedLocator(rowNumber, gridPage);
        String selectorStyle = rowElementMap.keySet().stream().toList().get(0);
        String locator = rowElementMap.values().stream().toList().get(0);
        return perform.getWebElement(selectorStyle, locator);
    }

    public HashMap<String, String> constructFieldSelector(String mainTag, By field, String... additionalSelector) {
        String[] splitString = field.toString().split(":");
        HashMap<String, String> selector = new HashMap<>();
        String constructedSelector = switch(splitString[0]) {
            case "By.id" -> "[id*=\"";
            case "By.name" -> "[name*=\"";
            case "By.className" -> "[class*=\"";
            case "By.cssSelector", "By.xpath" -> splitString[1];
            default -> throw new IllegalArgumentException("Invalid Argument");
        };
        if (splitString[0].equals("By.xpath") || splitString[0].equals("By.cssSelector")) {
            selector.put(splitString[0], splitString[1] + additionalSelector[0]);
        } else {
            selector.put("By.cssSelector", mainTag + constructedSelector + splitString[1].trim() + "\"]"
                    + additionalSelector[0]);
        }
        return selector;
    }
}
