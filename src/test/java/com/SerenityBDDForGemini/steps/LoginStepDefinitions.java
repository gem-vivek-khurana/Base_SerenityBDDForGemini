package com.SerenityBDDForGemini.steps;

import com.SerenityBDDForGemini.navigation.NavigateTo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggingEventBuilder;

import java.lang.reflect.InvocationTargetException;

public class LoginStepDefinitions {
    public final LoggingEventBuilder LOGGER_INFO = LoggerFactory.getLogger(LoginStepDefinitions.class).atInfo();

    @Given("I launch the browser and open the {string} login page")
    public void launchBrowserAndOpenTheLogin(String page) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        NavigateTo.class.getMethod("the" + page + "LoginPage").invoke(new NavigateTo());
    }

    @When("I stop the debugger here")
    public void iStopTheDebuggerHere() {
        System.out.println("Pause Run");
    }
}
