@csv-comparison
Feature: Comparing CSV files

Scenario Outline: Compare exported and transformed CSV files
  Given I have two CSV files "<csv1>" and "<csv2>"
  When I compare the CSV files
  Then I display the BODS data "<csv1>" as a tables
  Then I expect them to be "<result>"

Examples:
  | csv1                                      | csv2                                      | result |
  | C:\\Users\\ranji\\Documents\\docs\\1.csv     | C:\\Users\\ranji\\Documents\\docs\\2.csv | true   |
