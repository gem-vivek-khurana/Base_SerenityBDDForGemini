Feature: Login verification for Clovek Admin and Verify UI

  Scenario: Verify Login for Clovek
    Given I launch the browser and open the "Clovek" login page
    Then I should be on the "Clovek Login" page under the "Clovek" workflow
    When I set the "E-mail" text field as "admin@gemfin.in"
    And I set the "Password" text field as "Gemini#123"
    And I click the "Login" button
    Then I should be on the "Clovek Home" page under the "Clovek" workflow
#    When I click the "Add New Broker" button
#    Then I should be on the "Add New Broker" page under the "Clovek" workflow
#    When I set these fields with following values:
#      | field                                      | fieldType  | value                                     |
#      | Broker First Name                          | text field | Test Broker                               |
#      | Broker Last Name                           | text field | $CURRENT_DATE_FOR_NAME$                   |
#      | Email ID                                   | text field | $FAKE_DATA$ Email                         |
#      | Mobile Number                              | text field | $RANDOM_MOBILE_NUMBER$                    |
#      | Organization Name                          | text field | Test Organization $CURRENT_DATE_FOR_NAME$ |
#      | Provide broker admin with RM functionality | checkbox   | Checked                                   |
#    And I click the "Cancel" button
#    Then I should be on the "Clovek Home" page under the "Clovek" workflow
    When I focus on the "List Of Brokers" grid under the "Clovek" workflow
    Then I should see the following values in the grid:
      | row | Broker Name                         | Email ID                                      | Organization                              | RM Functionality |
      | 1   | Test Broker $CURRENT_DATE_FOR_NAME$ | testbroker$CURRENT_DATE_FOR_NAME$@yopmail.com | Test Organization $CURRENT_DATE_FOR_NAME$ | Yes              |
    When I click the "More" button on row 1 of the grid
    When I focus on the "Navigation" section under the "Clovek" workflow
    And I click the "Home" link
    Then I should be on the "Clovek Home" page under the "Clovek" workflow