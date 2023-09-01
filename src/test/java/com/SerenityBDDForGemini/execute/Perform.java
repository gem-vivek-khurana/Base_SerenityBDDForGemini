package com.SerenityBDDForGemini.execute;

import com.SerenityBDDForGemini.state.VerifyStateOf;
import com.SerenityBDDForGemini.support.DataObjectOperations;
import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public String gettingTextFieldValue(By byField) {
        WebElement fieldValueToGet = getDriver().findElement(byField);
        return fieldValueToGet.getAttribute("value");
    }

    public String gettingDropdownValue(By byField) {
        WebElement dropdownElement = getDriver().findElement(byField);
        return new Select(dropdownElement).getFirstSelectedOption().getText();
    }

    public String gettingRadioStatus(By byField) {
        if (getDriver().findElement(byField).isSelected()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    public String gettingRadioStatus(WebElement element) {
        if (element.isSelected()) {
            return "Selected";
        } else {
            return "Unselected";
        }
    }

    public String gettingCheckboxStatus(By byField) {
        if (gettingRadioStatus(byField).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
    }

    public String gettingCheckboxStatus(WebElement element) {
        if (gettingRadioStatus(element).equalsIgnoreCase("selected")) return "Checked";
        else return "Unchecked";
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

    public HashMap<String, String> gettingAlertState() {
        HashMap<String, String> alertState = new HashMap<>();
        WebElement alertElement = getDriver().findElement(By.cssSelector("div[role='alert'] div[class*='jq-toast']"));
        alertState.put("text", alertElement.getText());
        alertState.put("background-color", alertElement.getCssValue("background-color"));
        alertState.put("style", alertElement.getAttribute("style"));
        return alertState;
    }

    public String gettingBrowserAlertText() {
        String alertText = null;
        try {
            alertText = getDriver().switchTo().alert().getText();
        } catch (UnhandledAlertException ignored) {
        }
        return alertText;
    }

    public void acceptingBrowserAlert() {
        getDriver().switchTo().alert().accept();
    }

    public void dismissingBrowserAlert() {
        try {
            getDriver().switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {
        }
    }

    public String gettingFieldValue(Field field, Class<?> pageClass, FieldType fieldType) {
        By fieldValueToGet = fieldToInteract(field, pageClass);
        switch (fieldType) {
            case TEXT_FIELD, TEXTAREA, DATE_FIELD -> {
                return gettingTextFieldValue(fieldValueToGet);
            }
            case LABEL, DATE_LABEL -> {
                return gettingFieldValue(fieldValueToGet);
            }
            case DROPDOWN -> {
                return gettingDropdownValue(fieldValueToGet);
            }
            case RADIO -> {
                return gettingRadioStatus(fieldValueToGet);
            }
            case CHECKBOX -> {
                return gettingCheckboxStatus(fieldValueToGet);
            }
        }
        throw new RuntimeException("Unsupported field type: " + fieldType);
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

    public void pageRefresh() {
        getDriver().navigate().refresh();
    }

    public void headingToPreviousPage() {
        getDriver().navigate().back();
    }

    public Set<String> gettingWindowHandles() {
        try {
            // Wait for new window to open if expected
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return getDriver().getWindowHandles();
    }

    public void switchToWindowHandle(WindowHandleClassificationType type, String matchingParameter) {
        Set<String> windowHandles = gettingWindowHandles();
        if (windowHandles.size() == 1) {
            getDriver().switchTo().window((String) windowHandles.toArray()[0]);
        }
        boolean windowFound = false;
        for (int attempt = 1; attempt <= 20; attempt++) { // Attempt 20 times to switch and find the matching pattern
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (type.equals(WindowHandleClassificationType.TITLE_MATCH)) {
                for (String handle : windowHandles) {
                    getDriver().switchTo().window(handle);
                    if (getDriver().getTitle().contains(matchingParameter)) {
                        windowFound = true;
                        break;
                    }
                }
            } else if (type.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
                for (String handle : windowHandles) {
                    getDriver().switchTo().window(handle);
                    if (getDriver().getCurrentUrl().contains(matchingParameter)) {
                        windowFound = true;
                        break;
                    }
                }
            }
            if (windowFound) {
                LOGGER_INFO.log("Window found: TRUE. Switched to window with title: "
                        + getDriver().getTitle());
                break;
            }
            LOGGER_INFO.log("Attempt No." + attempt + " to locate window with handle of type: " + type);
        }
        if (!windowFound) {
            throw new RuntimeException("Unable to find window with type: " + type +
                    " and value: " + matchingParameter);
        }
    }

    public void closingWindow(WindowHandleClassificationType windowHandleClassificationType, String matchingParameter) {
        boolean rightWindowFocused = false;
        if (windowHandleClassificationType.equals(WindowHandleClassificationType.TITLE_MATCH)) {
            rightWindowFocused = getDriver().getTitle().equals(matchingParameter);
        } else if (windowHandleClassificationType.equals(WindowHandleClassificationType.URL_SUBSTRING)) {
            rightWindowFocused = getDriver().getCurrentUrl().contains(matchingParameter);
        }
        if (!rightWindowFocused) {
            switchToWindowHandle(windowHandleClassificationType, matchingParameter);
        }
        getDriver().close();
    }

    public enum WindowHandleClassificationType {
        TITLE_MATCH,
        URL_SUBSTRING
    }

    public enum FieldType {
        TEXT_FIELD,
        DATE_FIELD,
        DATE_LABEL,
        DROPDOWN,
        CHECKBOX,
        RADIO,
        LABEL,
        TEXTAREA;

        public static FieldType resoluteFieldType(String fieldType) {
            return switch (fieldType.toLowerCase()) {
                case "text field" -> TEXT_FIELD;
                case "date field" -> DATE_FIELD;
                case "date label" -> DATE_LABEL;
                case "radio" -> RADIO;
                case "checkbox" -> CHECKBOX;
                case "dropdown" -> DROPDOWN;
                case "label" -> LABEL;
                case "textarea" -> TEXTAREA;
                default -> throw new RuntimeException("Unsupported field type: " + fieldType);
            };
        }

        public static String resoluteFieldName(FieldType fieldType) {
            return switch (fieldType) {
                case TEXT_FIELD -> "text field";
                case DATE_FIELD -> "date field";
                case DATE_LABEL -> "date label";
                case RADIO -> "radio";
                case CHECKBOX -> "checkbox";
                case DROPDOWN -> "dropdown";
                case LABEL -> "label";
                case TEXTAREA -> "textarea";
            };
        }
    }
}
