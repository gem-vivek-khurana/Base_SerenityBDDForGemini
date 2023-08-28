package com.SerenityBDDForGemini.pages.clovek;

import com.SerenityBDDForGemini.support.PageObjectOperations;
import org.openqa.selenium.By;

public class ClovekHome extends PageObjectOperations {
    public static final By PAGE_LOADED = By.tagName("app-product-admin-dashboard");
    public static final By ADD_NEW_BROKER = By.id("submit-button");
}
