package com.SerenityBDDForGemini.pages.clovek.grids;

import com.SerenityBDDForGemini.support.GridObjectOperations;
import org.openqa.selenium.By;

public class ListOfBrokers extends GridObjectOperations {
    public static final By GRID_LOADED = By.cssSelector("app-product-admin-dashboard table");
    public static final By SEARCH = By.cssSelector("input.search-text");
    public static final By MORE = By.className("mat-menu-trigger");
    public static final By RESEND_INVITE = By.xpath(
            "//button[@role='menuitem']/span[text()='Resend Invite']/parent::button");
}
