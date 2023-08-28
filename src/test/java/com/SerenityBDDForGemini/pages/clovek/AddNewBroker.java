package com.SerenityBDDForGemini.pages.clovek;

import com.SerenityBDDForGemini.support.PageObjectOperations;
import org.openqa.selenium.By;

public class AddNewBroker extends PageObjectOperations {
    public static final By PAGE_LOADED = By.tagName("app-add-new-broker");
    public static final By BROKER_FIRST_NAME = By.cssSelector("input[formcontrolname='firstName']");
    public static final By BROKER_MIDDLE_NAME = By.cssSelector("input[formcontrolname='middleName']");
    public static final By BROKER_LAST_NAME = By.cssSelector("input[formcontrolname='lastName']");
    public static final By EMAIL_ID = By.cssSelector("input[formcontrolname='email']");
    public static final By MOBILE_NUMBER = By.cssSelector("input[formcontrolname='phoneNo']");
    public static final By ORGANIZATION_NAME = By.cssSelector("input[formcontrolname='organizationName']");
    public static final By PROVIDE_BROKER_ADMIN_WITH_RM_FUNCTIONALITY = By.cssSelector("mat-checkbox[formcontrolname='hasRMFunctionality']");
    public static final By CANCEL = By.className("cancel-button");
    public static final By ADD_NEW_BROKER = By.id("submit-button-add-broker");
}
