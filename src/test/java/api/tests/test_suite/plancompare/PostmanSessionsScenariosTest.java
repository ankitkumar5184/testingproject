package api.tests.test_suite.plancompare;

import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class PostmanSessionsScenariosTest {
    PostRequest postRequest = new PostRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    String SessionId;
    public String getMedGapSessionId;
    public String getDentalVisionSessionId;
    public String getNpiNumber;

    @Test(priority = 0)
    public void testCreateSession() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String createSessionEndpoint = prop.getProperty("Sessions_CreateSession_Postman").replace("{SessionID}", "%s");
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionsData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") + String.format(createSessionEndpoint, getSessionId()),
                fileReader.getTestJsonFile(createSessionData));
        String getMBI = response.jsonPath().get("Profile.MBI");
        String getZip = response.jsonPath().get("Zip");
        String getFips = response.jsonPath().get("Fips");
        String getGenericLabelName = response.jsonPath().get("Dosages[0].GenericLabelName");
        String getBirthDate = response.jsonPath().get("Birthdate");
        assertEquals(getZip, ZipCodeValues.Zip_90011.value);
        assertEquals(getFips, FipsValues.Fips_06037.value);
        assertEquals(getMBI, dataProp.getProperty("MBI"));
        assertEquals(getBirthDate, dataProp.getProperty("BIRTH_DATE"));
        assertEquals(getGenericLabelName, dataProp.getProperty("GENERIC_LABEL_NAME"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session Response : " + response.asPrettyString());
    }

    @Test(priority = 1)
    public void testCreateSessionWithProfile() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String postCreateSessionWithProfileData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithProfile.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProfileData));
        String getReqId = response.jsonPath().get("[0].reqid");
        String getSubmittedNDC = response.jsonPath().getString("[0].Dosages[1].SubmittedNDC");
        assertEquals(getSubmittedNDC, dataProp.getProperty("SubmittedNDC"));
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With Profile Response : " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testCreateSessionWithNoProfile() {
        String postCreateSessionWithNoProfileData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithNoProfile.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithNoProfileData));
        String getReqId = response.jsonPath().get("[0].reqid");
        String getSubmittedNDC = response.jsonPath().getString("[0].Dosages[1].SubmittedNDC");
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(getSubmittedNDC, dataProp.getProperty("SubmittedNDC"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With No Profile Response : " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testCreateSessionWithQuestions() {
        String postCreateSessionWithQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithQuestionsData));
        String getStatus = response.jsonPath().get("[0].Status");
        String getSubmittedNDC = response.jsonPath().getString("[0].Dosages[1].SubmittedNDC");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(getSubmittedNDC, dataProp.getProperty("SubmittedNDC"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With Questions Response : " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testCreateSessionWithSSOValueOnly() {
        String postCreateSessionWithSSOValueOnlyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithSSOValueOnly.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithSSOValueOnlyData));
        String getSSOValue = response.jsonPath().get("[0].SSOValue");
        assertEquals(getSSOValue, dataProp.getProperty("SSOValue"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With SSOValue Only Response : " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testCreateSessionWithXML() {
        String postCreateSessionWithXMLData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithXML.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithXMLData));
        String getStatus = response.jsonPath().get("[0].Status");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With XML Response : " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testCreateSessionWithXMLWithBlankBodyData() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("Sessions_CreateSession"),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Create Session With XML Blank Body Data Response : " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testCreateSessionWithProfilePlus() {
        String postCreateSessionWithProfilePlusData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithProfile+.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProfilePlusData));
        String getStatus = response.jsonPath().get("[0].Status");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With Profile Plus Response : " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testCreateSessionWithProviders() {
        String postCreateSessionWithProvidersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithProviders.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProvidersData));
        String getAddressId = response.jsonPath().get("[0].Providers[0].AddressId");
        getNpiNumber = response.jsonPath().get("[0].Providers[0].NPI");
        assertEquals(getAddressId, dataProp.getProperty("ADDRESS_ID"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With Providers Response : " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testCreateSessionWithOneBrandDrug() {
        String postCreateSessionWithOneBrandDrugData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithOneBrandDrug.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithOneBrandDrugData));
        String getNDC = response.jsonPath().get("[0].Dosages[0].SubmittedNDC");
        assertEquals(getNDC, dataProp.getProperty("NDC"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With One Brand Drug Response : " + response.asPrettyString());
    }

    @Test(priority = 10)
    public void testCreateSessionWithMedGapQuestions() {
        String postCreateSessionWithMedGapQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithMedGapQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithMedGapQuestionsData));
        String getStatus = response.jsonPath().get("[0].Status");
        getMedGapSessionId = response.jsonPath().get("[0].SessionID");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With MedGap Questions Response : " + response.asPrettyString());
    }

    @Test(priority = 11)
    public void testCreateSessionWithDentalVisionQuestions() {
        String postCreateSessionWithDentalVisionQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionWithDentalVisionQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithDentalVisionQuestionsData));
        String getStatus = response.jsonPath().get("[0].Status");
        getDentalVisionSessionId = response.jsonPath().get("[0].SessionID");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With MedGap Questions Response : " + response.asPrettyString());
    }

    @Test(priority = 12)
    public void testUpdateSessionsWithQuestions() {
        String postUpdateSessionsWithQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSessionWithQuestions.json";
        String updateSessionQuestionsEndpoint = prop.getProperty("Sessions_CreateSession_Postman").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                        String.format(updateSessionQuestionsEndpoint, SessionId), fileReader.getTestJsonFile(postUpdateSessionsWithQuestionsData));
        String getPharmacyId = response.jsonPath().get("Pharmacies[1].PharmacyID");
        String getNDC = response.jsonPath().get("Dosages[1].NDC");
        assertEquals(getPharmacyId, dataProp.getProperty("SESSION_PHARMACY_ID"));
        assertEquals(getNDC, dataProp.getProperty("NDC_NUMBER"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create Session With MedGap Questions Response : " + response.asPrettyString());
    }

    private String getSessionId() {
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionData.json";
        SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return SessionId;
    }
}