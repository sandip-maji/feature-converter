package com.bdd.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class StepDefinitions {

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        System.out.println("Given the user is on the login page");
    }

    @Then("the user is redirected to the dashboard")
    public void theUserIsRedirectedToTheDashboard() {
        System.out.println("Then the user is redirected to the dashboard");
    }

    @Then("the user sees an error message")
    public void theUserSeesAnErrorMessage() {
        System.out.println("Then the user sees an error message");
    }

    @Then("the user is logged out")
    public void theUserIsLoggedOut() {
        System.out.println("Then the user is logged out");
    }
}
