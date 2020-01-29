Feature: Login

    Scenario: Login as administrator user
        When I login as user 'admin' and password 'admin'
        Then the user is found
        And the home page opens
