package stepDefinitions;

import common.CSVUtils;
import common.DBHelper;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseStepDefinitions {
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    private Scenario scenario;

    private String comparisonResult;
    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;

    }

    @Given("I have database driver, url, user and password")
    public void i_have_database_driver_url_user_and_password() {
        try {
            DBHelper.setupDBConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver", e);
        }
    }

    @When("I establish a connection to the database")
    public void i_establish_a_connection_to_the_database() {
        try {
            DBHelper.establishDBConnection();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    @Then("I should be connected successfully")
    public void i_should_be_connected_successfully() {
        if (conn == null) {
            throw new RuntimeException("Failed to establish a connection to the database");
        }
    }



    @Then("I display the BODS data {string} as a tables")
    public void i_display_Bods_data_asTable(String path) {
       ArrayList csvData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                List<String> row = new ArrayList<>();
                for (String data : rowData) {
                    row.add(data);
                }
                csvData.add(row);
                DataTable table = DataTable.create(csvData);
                table.diff(DataTable.create(csvData));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @When("I execute a query and export data {string}")
    public void i_execute_a_query(String exportPath) {
        try {
            DBHelper.executeSQLandExport(exportPath);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to execute the query", e);
        }
    }

    @Then("I display the database data {string} as a table$")
    public void i_display_database_data_asTable(String path) {
        ArrayList csvData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                List<String> row = new ArrayList<>();
                for (String data : rowData) {
                    row.add(data);
                }
                csvData.add(row);
                DataTable table = DataTable.create(csvData);
                table.diff(DataTable.create(csvData));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("I compare the results between {string} and {string}")
    public void i_should_get_the_result_successfully(String sourcePath,String destinationPath) throws IOException {
         comparisonResult = CSVUtils.compareCSVs(sourcePath,destinationPath);

    }

    @After
    public void tearDown() throws SQLException {
        if(comparisonResult.trim().length()==0)
            scenario.write("Both the files are Same...");
        scenario.write(comparisonResult);
        DBHelper.close();
    }
}

