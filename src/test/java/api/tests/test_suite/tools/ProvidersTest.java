package api.tests.test_suite.tools;

import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import static api.base.helpers.CommonMethods.dataProp;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertNotNull;

public class ProvidersTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String providerEndpoint, providerEndPointData;

    @Test
    public void testVerifyProviderSearch() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/providerSearch.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("Total"));
        assertNotNull(response.jsonPath().getString("Providers[0].Id"));
        assertEquals(response.jsonPath().getString("Providers[0].Gender"), dataProp.getProperty("GENDER_FEMALE"));
        assertEquals(response.jsonPath().getString("Providers[0].FirstName"), dataProp.getProperty("FIRST_NAME"));
        assertEquals(response.jsonPath().getString("Providers[0].LastName"), dataProp.getProperty("LAST_NAME"));
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }

    @Test(priority = 1)
    public void testVerifyProviderSearchWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testVerifyProviderSearchByLatitudeLongitude(){
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/providerSearchByLatitudeLongitude.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("Total"));
        assertEquals(response.jsonPath().getString("Providers[0].Gender"), dataProp.getProperty("GENDER_MALE"));
        assertEquals(response.jsonPath().getString("Providers[0].FirstName"), dataProp.getProperty("ProviderSearchByLatitudeLongitude_FIRST_NAME"));
        assertNotNull(response.jsonPath().getString("Providers[0].Addresses[0].ZipCode"));
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testVerifyProviderSearchByLatitudeLongitudeWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testVerifyProviderSearchByProviderSearchByID(){
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/providerSearchByID.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
        assertNotNull(response.jsonPath().getString("Total"));
        assertEquals(response.jsonPath().getString("Providers[0].Id"), dataProp.getProperty("NPIs_ID"));
        assertNotNull(response.jsonPath().getString("Providers[0].FirstName"));
        assertNotNull(response.jsonPath().getString("Providers[0].LastName"));
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testVerifyProviderSearchByIDWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
        ExtentLogger.pass("SessionLessProviderSearch response:- " + response.asPrettyString());
    }
}