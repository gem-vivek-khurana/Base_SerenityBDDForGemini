package com.SerenityBDDForGemini.execute;

import com.SerenityBDDForGemini.state.VerifyStateOf;
import com.SerenityBDDForGemini.support.DataObjectOperations;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Perform extends PageObject {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(Perform.class).atInfo();
    final VerifyStateOf verifyStateOf = new VerifyStateOf();

    public By fieldToInteract(Field field, Class<?> pageClass) {
        try {
            return (By) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void settingFieldValue(Field field, Class<?> pageClass, String value) {
        By fieldToFill = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(fieldToFill));
        settingFieldValue(fieldToFill, value);
    }

    public void settingFieldValue(By byField, String value) {
        getDriver().findElement(byField).sendKeys(value);
    }

    public void clickOn(Field field, Class<?> pageClass) {
        By fieldToClick = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.elementToBeClickable(fieldToClick));
        clickOn(fieldToClick);
    }

    public void clickOn(By byField) {
        WebElement elementToClick = getDriver().findElement(byField);
        clickOn(elementToClick);
    }

    public void clickOn(WebElement elementToClick) {
        elementToClick.click();
    }

    public WebElement getWebElement(Field field, Class<?> pageClass) {
        By byElement = fieldToInteract(field, pageClass);
        return getWebElement(byElement);
    }

    public WebElement getWebElement(By byElement) {
        return getDriver().findElement(byElement);
    }

    public WebElement getWebElement(String selectorStyle, String locator) {
        switch (selectorStyle) {
            case "By.cssSelector" -> {
                return getWebElement(By.cssSelector(locator));
            }
            case "By.xpath" -> {
                return getWebElement(By.xpath(locator));
            }
            default -> throw new IllegalArgumentException("Invalid selector type in the arguments");
        }
    }

    public List<WebElement> getWebElements(Field field, Class<?> pageClass) {
        By byElement = fieldToInteract(field, pageClass);
        return getWebElements(byElement);
    }

    public List<WebElement> getWebElements(By element) {
        return getDriver().findElements(element);
    }

    public String gettingFieldValue(By byField) {
        WebElement fieldValueToGet = getDriver().findElement(byField);
        String value = fieldValueToGet.getAttribute("value");
        value = value == null ? fieldValueToGet.getText() : value;
        return value;
    }

    public void settingDateFieldValue(Field field, Class<?> pageClass, String value) {
        By fieldToFill = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(fieldToFill));
        settingDateFieldValue(fieldToFill, value);
    }

    public void settingDateFieldValue(By byField, String value) {
        getDriver().findElement(byField).clear();
        ArrayList<DateTimeFormatter> formatters = DataObjectOperations.DateTimeFormatters.getFormattersList();
        String separator = "";
        LocalDate date;
        for (DateTimeFormatter formatter : formatters) {
            try {
                date = LocalDate.parse(value, formatter);
                String formattedDate = formatter.format(date);
                separator = formattedDate.contains("/") ? "/" : "-";
                if (separator.equals("-")) separator = formattedDate.contains("-") ? "-" : "";
                break;
            } catch (Exception ignored) {
            }
        }
        String[] splitValue = value.split(separator);
        for (String val : splitValue) {
            getDriver().findElement(byField).sendKeys(val);
        }
        getDriver().findElement(byField).sendKeys(Keys.ESCAPE);
        if (!gettingFieldValue(byField).contains(separator)) {
            getDriver().findElement(byField).clear();
            settingFieldValue(byField, value);
        }
    }

    public void settingDropdownValue(Field field, Class<?> pageClass, String value) {
        By fieldToSelect = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(fieldToSelect));
        settingDropdownValue(fieldToSelect, value);
    }

    public void settingDropdownValue(By byField, String value) {
        Select field = new Select(getDriver().findElement(byField));
        field.selectByVisibleText(value);
    }

    public void settingCheckboxAs(Field field, Class<?> pageClass, String value) {
        By checkboxField = fieldToInteract(field, pageClass);
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(checkboxField));
        settingCheckboxAs(checkboxField, value);
    }

    public void settingCheckboxAs(By byField, String value) {
        WebElement checkbox = getDriver().findElement(byField);
        settingCheckboxAs(checkbox, value);
    }

    public void settingCheckboxAs(WebElement checkbox, String value) {
        boolean checkboxState = verifyStateOf.checkboxIsSelected(checkbox);
        boolean expectedState = value.equalsIgnoreCase("checked");
        if ((!checkboxState && expectedState) || (checkboxState && !expectedState)) {
            checkbox.click();
        } else if (checkboxState == expectedState) {
            LOGGER_INFO.log("Checkbox state is already as expected i.e.: " + value);
        } else if (!value.equalsIgnoreCase("checked") && !value.equalsIgnoreCase("unchecked")){
            throw new IllegalArgumentException("Incorrect value provided for setting the checkbox. It should " +
                    "either be 'Checked' or 'Unchecked' (Case-Insensitive)");
        } else {
            throw new RuntimeException("Unable to " + value.replace("ed", "") + " checkbox");
        }
    }

    public HashMap<String, Boolean> gettingFieldState(Field field, Class<?> pageClass) {
        By fieldForState = fieldToInteract(field, pageClass);
        return gettingFieldState(fieldForState);
    }

    public HashMap<String, Boolean> gettingFieldState(By byField) {
        HashMap<String, Boolean> fieldState = new HashMap<>();
        WebElement element = null;
        try {
            element = getDriver().findElement(byField);
            fieldState.put("not visible", !element.isDisplayed());
            fieldState.put("visible", element.isDisplayed());
        } catch (NoSuchElementException e) {
            fieldState.put("not visible", true);
        }
        if (element != null) {
            fieldState.put("visible", element.isDisplayed());
            try {
                fieldState.put("readonly", !element.isEnabled());
                fieldState.put("selected", element.isSelected());
            } catch (Exception e) {
                LOGGER_INFO.log("Attempt to perform an unsupported operation for the field.");
            }
        }
        return fieldState;
    }
}
