package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.reporter.ExtentLogger;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class DrugsTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    @Test(priority = 1)
    public void testVerifyGetDrugSearch() {
        commonMethods.usePropertyFileData("TOOLS-API");
        commonMethods.usePropertyFileForEndpoints();
        String getDrugSearchEndpoint = prop.getProperty("Drugs_GetDrugSearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getDrugSearchEndpoint + dataProp.getProperty("Drugs_GetDrugSearchData"));
        ExtentLogger.pass("Test DrugsTest get drug search case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int drugId = Integer.parseInt(dataProp.getProperty("DrugsTest_DrugsSearch_DrugId"));
        assertEquals(response.jsonPath().getInt("[0].DrugID"), drugId);
        assertEquals(response.jsonPath().get("[0].DrugType"), dataProp.getProperty("DrugsTest_DrugsSearch_DrugType"));
    }

    @Test(priority = 2)
    public void testVerifyGetAutocomplete() {
        String getAutocompleteEndpoint = prop.getProperty("Drugs_GetAutocomplete");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getAutocompleteEndpoint + dataProp.getProperty("Drugs_GetAutocompleteData"));
        ExtentLogger.pass("Test DrugsTest get autocomplete case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testVerifyGetDrugDetails() {
        String getDrugDetailsEndpoint = prop.getProperty("Drugs_GetDrugDetails").replace("{DrugID}", dataProp.getProperty("Drugs_GetDrugDetailsData"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getDrugDetailsEndpoint);
        ExtentLogger.pass("Test DrugsTest get drug details case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int drugId = Integer.parseInt(dataProp.getProperty("DrugsTest_DrugDetails_DrugID"));
        assertEquals(response.jsonPath().getInt("DrugID"), drugId);
        assertEquals(response.jsonPath().get("Dosages[0].DosageID"), dataProp.getProperty("DrugsTest_DrugDetails_DosageID"));
        assertEquals(response.jsonPath().get("Dosages[0].ReferenceNDC"), dataProp.getProperty("DrugsTest_DrugDetails_ReferenceNDC"));
        assertEquals(response.jsonPath().get("Dosages[0].LabelName"), dataProp.getProperty("DrugsTest_DrugDetails_LabelName"));
    }

    @Test(priority = 4)
    public void testVerifyGetDrugDetailsByNDC() {
        String getDrugDetailsByNDCEndpoint = prop.getProperty("Drugs_GetDrugDetailsByNDC");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getDrugDetailsByNDCEndpoint + dataProp.getProperty("Drugs_GetDrugDetailsByNDC_Data"));
        ExtentLogger.pass("Test DrugsTest get drug details by NDC case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("Dosages[0].ReferenceNDC"), dataProp.getProperty("DrugsTest_DrugDetailsNDC_ReferenceNDC"));
        int drugId = Integer.parseInt(dataProp.getProperty("DrugsTest_DrugDetailsNDC_DrugId"));
        assertEquals(response.jsonPath().getInt("DrugID"), drugId);
        assertEquals(response.jsonPath().get("Dosages[0].LabelName"), dataProp.getProperty("DrugsTest_DrugDetailsNDC_LabelName"));
    }

    @Test(priority = 5)
    public void testVerifyGetDrugDosages() {
        String getDrugDosagesEndpoint = prop.getProperty("Drugs_GetDrugDosages").replace("{DrugID}", dataProp.getProperty("Drugs_GetDosage_DrugID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getDrugDosagesEndpoint);
        ExtentLogger.pass("Test DrugsTest get drug dosages case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].DosageID"), dataProp.getProperty("DrugsTest_GetDrugDosages_DosageID"));
    }

    @Test(priority = 6)
    public void testVerifyGetDosagesDetails() {
        String getDosagesDetailsEndpoint = prop.getProperty("Drugs_GetDosagesDetails").replace("{DrugID}", dataProp.getProperty("Drugs_GetDosage_DrugID")).replace("{DosageID}", dataProp.getProperty("Drugs_GetDosagesDetailsData"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getDosagesDetailsEndpoint);
        ExtentLogger.pass("Test DrugsTest get dosages details case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("DosageID"), dataProp.getProperty("DrugsTest_GetDosagesDetails_DosageID"));
        assertEquals(response.jsonPath().get("ReferenceNDC"), dataProp.getProperty("DrugsTest_GetDosagesDetails_ReferenceNDC"));
    }
}