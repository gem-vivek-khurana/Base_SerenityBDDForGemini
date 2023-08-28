package com.SerenityBDDForGemini.steps;

import com.SerenityBDDForGemini.state.VerifyStateOf;
import com.SerenityBDDForGemini.support.PageObjectOperations;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

import java.lang.reflect.Field;

public class PageStateVerificationStepDefinitions {
    @Steps
    VerifyStateOf verifyStateOf;
    @Steps
    PageObjectOperations pageObjectOperations;

    @Then("I should be on the {string} page of/under the {string} workflow")
    public void iShouldBeOnThePageUnderTheWorkflow(String page, String workflow) throws IllegalAccessException {
        Class<?> pageClass = pageObjectOperations.getPageClass(page, workflow);
        Field field = pageObjectOperations.poeFieldClass("PAGE_LOADED", page, workflow);
        verifyStateOf.theVisibilityOf(field, pageClass);
        Serenity.setSessionVariable("Current Page").to(pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + "." + pageObjectOperations.pageObjectName(page));
    }

    @When("I focus on the {string} section under the {string} workflow")
    public void iFocusOnSectionUnderTheWorkflow(String section, String workflow) throws IllegalAccessException {
        Class<?> sectionClass = pageObjectOperations.getSectionClass(section, workflow);
        Field sectionLoaded = pageObjectOperations.poeSectionClass("SECTION_LOADED", section, workflow);
        verifyStateOf.theVisibilityOf(sectionLoaded, sectionClass);
        Serenity.setSessionVariable("Current Page").to(pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".section." + pageObjectOperations.pageObjectName(section));
    }

    @When("I focus on the {string} grid under the {string} workflow")
    public void iFocusOnTheGridUnderTheWorkflow(String grid, String workflow) throws IllegalAccessException {
        Class<?> gridClass = pageObjectOperations.getGridClass(grid, workflow);
        Field field = pageObjectOperations.poeGridClass("GRID_LOADED", grid, workflow);
        verifyStateOf.theVisibilityOf(field, gridClass);
        Serenity.setSessionVariable("Current Page").to(pageObjectOperations.pageObjectName(workflow)
                .toLowerCase() + ".grids." + pageObjectOperations.pageObjectName(grid));
    }
}
