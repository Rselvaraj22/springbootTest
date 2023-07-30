package stepDefinitions;

import common.CSVUtils;
import common.DBUtills;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVComparisonSteps {
    private String csv1Path;
    private String csv2Path;
    private String comparisonResult;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;

    }
    @Given("I have two CSV files {string} and {string}")
    public void iHaveTwoCSVFiles(String csv1Path, String csv2Path) {
        this.csv1Path = csv1Path;
        this.csv2Path = csv2Path;
    }
    @When("I compare the CSV files")
    public void iCompareTheCSVFiles() throws IOException {
        comparisonResult = CSVUtils.compareCSVs(csv1Path,csv2Path);
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

    @Then("I expect them to be {string}")
    public void iExpectThemToBe(String result) {
        Assert.assertEquals("CSV files comparison result should match the expected result", 0, comparisonResult.length());
    }

    @After
    public void tearDown() {
        if(comparisonResult.trim().length()==0)
            scenario.write("Both the files are Same...");
        scenario.write(comparisonResult);
    }
}