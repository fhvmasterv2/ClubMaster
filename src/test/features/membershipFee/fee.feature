Feature: Membership Fee

    Scenario: Retrieve unpaid fees
        When I search user 'admin'
        Then the user is found
        And his last name is 'Administrator'
