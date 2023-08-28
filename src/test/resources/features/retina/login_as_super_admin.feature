Feature: Login to Retina as Super Admin and Verify UI

  Scenario: Verify Login for Retina
    Given I launch the browser and open the "Retina" login page
    Then I should be on the "Retina Login" page under the "Retina" workflow
    When I set the "Email" text field as "riversray243@gmail.com"
    And I set the "Password" text field as "Password@123"
    And I click the "Login" button
    And I stop the debugger here
    Then I should be on the "Retina Home" page under the "Retina" workflow