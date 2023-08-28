package com.SerenityBDDForGemini.pages.clovek;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("http://dev.clovek.frontend.s3-website.ap-south-1.amazonaws.com/")
public class ClovekLogin extends PageObject {
    public static final By PAGE_LOADED = By.tagName("app-login-screen");
    public static final By E_MAIL = By.cssSelector("input[formcontrolname='email']");
    public static final By PASSWORD = By.cssSelector("input[formcontrolname='password']");
    public static final By LOGIN = By.className("loginbutton");
    public static final By FORGOT_YOUR_PASSWORD = By.className("to-new-page");
}
