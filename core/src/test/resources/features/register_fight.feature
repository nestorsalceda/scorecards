Feature: Register a fight

  Scenario: successfully if all is ok
    Given an existing boxer called "Kerman Lejarraga"
    And an existing boxer called "Bradley Skeete"
    And an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete"
    Then the fight is successfully registered

  Scenario: fails if the first boxer is not present
    Given an existing boxer called "Bradley Skeete"
    And an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "" and "Bradley Skeete"
    Then the fight is not registered

  Scenario: fails if the second boxer is not present
    Given an existing boxer called "Kerman Lejarraga"
    And an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and ""
    Then the fight is not registered
    
  Scenario: fails without event date and place
    Given an existing boxer called "Kerman Lejarraga"
    And an existing boxer called "Bradley Skeete"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete"
    Then the fight is not registered