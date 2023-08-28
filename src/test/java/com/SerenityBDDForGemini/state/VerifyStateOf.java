package com.SerenityBDDForGemini.state;

import net.serenitybdd.core.pages.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.lang.reflect.Field;

public class VerifyStateOf extends PageObject {

    public void theVisibilityOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        By elementForVisibility = (By) field.get(pageClass);
        theVisibilityOf(elementForVisibility);
    }

    public void theVisibilityOf(By byField) {
        waitForCondition().until(ExpectedConditions.visibilityOfElementLocated(byField));
    }

    public void theVisibilityOf(WebElement element) {
        waitForCondition().until(ExpectedConditions.visibilityOf(element));
    }

    public boolean elementIsVisible(By byField) {
        try {
            return getDriver().findElement(byField).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void thePresenceOf(Field field, Class<?> pageClass) throws IllegalAccessException {
        By elementForAvailability = (By) field.get(pageClass);
        thePresenceOf(elementForAvailability);
    }

    public void thePresenceOf(By byField) {
        waitForCondition().until(ExpectedConditions.presenceOfElementLocated(byField));
    }

    public boolean elementIsPresent(By byField) {
        try {
            return getDriver().findElements(byField).size() > 0;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean visibilityOfElementInViewPort(WebElement element) {
        WebDriver driver = getDriver();

        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;                            "
                , element);
    }

    public void theInvisibilityOf(Field field, Class<?> pageClass) {
        By elementForInvisibility;
        try {
            elementForInvisibility = (By) field.get(pageClass);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        theInvisibilityOf(elementForInvisibility);
    }

    public void theInvisibilityOf(By byField) {
        waitForCondition().until(ExpectedConditions.invisibilityOfElementLocated(byField));
    }

    public boolean checkboxIsSelected(WebElement checkbox) {
        if (checkbox.getTagName().contains("input")) {
            return checkbox.isSelected();
        }
        return checkbox.findElement(By.className("mat-checkbox-input"))
                .getAttribute("aria-checked").equals("true");
    }
}
