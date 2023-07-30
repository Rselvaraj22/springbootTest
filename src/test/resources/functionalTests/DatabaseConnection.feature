@database-connection
Feature: Database Connection and Query Execution
  As a user, I want to connect to the database and execute a query to ensure everything is working correctly

  Scenario: Connect to the database and execute a query
    Given I have database driver, url, user and password
    When I establish a connection to the database
    Then I should be connected successfully
    Then I display the BODS data "C:\\Users\\ranji\\Documents\\docs\\2.csv" as a table
    When I execute a query and export data "C:\\Users\\ranji\\Documents\\docs\\exportpath.csv"
    Then I display the database data "C:\\Users\\ranji\\Documents\\docs\\exportpath.csv" as a table
    Then I compare the results between "C:\\Users\\ranji\\Documents\\docs\\exportpath.csv" and "C:\\Users\\ranji\\Documents\\docs\\2.csv"

