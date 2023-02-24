package api.tests.test_suite.plancompare;

import api.base.core.DeleteRequest;
import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;

public class SessionDosagesTest {

    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    PutRequest putRequest = new PutRequest();
    DeleteRequest deleteRequest = new DeleteRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    String testAddSessionDosagesData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/testAddSessionDosages.xml";
    String testUpdateSingleSessionDosageData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/testUpdateSingleSessionDosages.json";
    String testUpdateSessionDosageData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/testUpdateSessionDosage.json";
    String testAdd25SessionDosagesData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/testAdd25DosagesToSessionDosages.json";

    protected String sessionDosagesEndpoint;
    protected String dosagesSessionId, getDosageRecordID, getDrugId;
    protected String dosageRecordId, dosageRecordIdToDelete;

    @Test(priority = 1)
    public void testVerifyAddSessionDosages() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, getSessionDosagesSessionId()),
                fileReader.getTestJsonFile(testAddSessionDosagesData));
        String getNDC = response.jsonPath().getString("[0].NDC");
        getDrugId = response.jsonPath().getString("[0].DrugID");
        getDosageRecordID = response.jsonPath().getString("[0].DosageRecordID");
        Assert.assertEquals(getNDC, dataProp.getProperty("NDC_30698006001"));
        ExtentLogger.pass("Test add session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyAddSessionDosagesWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(testAddSessionDosagesData));
        ExtentLogger.pass("Test add session dosages response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyAddSessionDosagesWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(testAddSessionDosagesData));
        ExtentLogger.pass("Test add session dosages response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 4)
    public void testVerifyAddSessionDosagesWithEmptyBody() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        ExtentLogger.pass("Test add session dosages response when empty body:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.dosages[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 5)
    public void testVerifyGetSessionDosages() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_GetSessionDosages").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId));
        ExtentLogger.pass("Test get session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        dosageRecordId = response.jsonPath().getString("[0].DosageRecordID");
        Assert.assertEquals(dosageRecordId, getDosageRecordID);
        Assert.assertEquals(response.jsonPath().getString("[0].DrugID"), getDrugId);
    }

    @Test(priority = 6)
    public void testVerifyGetSessionSingleDosage() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_GetSessionDosages").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId) + dosageRecordId);
        ExtentLogger.pass("Test get single session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("DosageRecordID"), dosageRecordId);
    }

    @Test(priority = 7)
    public void testVerifyGetSessionDosagesWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_GetSessionDosages").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")));
        ExtentLogger.pass("Test add session dosages response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testVerifyGetSessionDosagesWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_GetSessionDosages").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")));
        ExtentLogger.pass("Test add session dosages response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyUpdateSingleSessionDosage() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateSingleDosage").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId) + dosageRecordId,
                fileReader.getTestJsonFile(testUpdateSingleSessionDosageData));
        ExtentLogger.pass("Test update single session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("DosageRecordID"), dosageRecordId);
        Assert.assertEquals(response.jsonPath().getString("DaysOfSupply"), dataProp.getProperty("DAYS_OF_SUPPLY_SINGLE_SESSION_DOSAGES"));
    }

    @Test(priority = 10)
    public void testVerifyUpdateSingleSessionDosageWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateSingleDosage").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT"))
                + dosageRecordId, fileReader.getTestJsonFile(testUpdateSingleSessionDosageData));
        ExtentLogger.pass("Test update single session dosages when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 11)
    public void testVerifyUpdateSingleSessionDosageWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateSingleDosage").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dosageRecordId, fileReader.getTestJsonFile(testUpdateSingleSessionDosageData));
        ExtentLogger.pass("Test update single session dosages when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 12)
    public void testVerifyUpdateSingleSessionDosageWithEmptyBody() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId) + dosageRecordId,
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test update single session dosages when empty body:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("DosageRecordID"), dosageRecordId);
    }

    @Test(priority = 13)
    public void testVerifyUpdateSessionDosages() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateDosages").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId),
                fileReader.getTestJsonFile(testUpdateSessionDosageData));
        ExtentLogger.pass("Test update session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        dosageRecordIdToDelete = response.jsonPath().getString("[0].DosageRecordID");
    }

    @Test(priority = 14)
    public void testVerifyUpdateSessionDosagesWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateDosages").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(testUpdateSessionDosageData));
        ExtentLogger.pass("Test update session dosages when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifyUpdateSessionDosagesWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateDosages").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(testUpdateSessionDosageData));
        ExtentLogger.pass("Test update session dosages when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 16)
    public void testVerifyUpdateSessionDosagesWithEmptyBody() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_UpdateDosages").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId),
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test update session dosages when empty body:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.dosages[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 17)
    public void testVerifyRemoveSingleSessionDosage() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosage").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId) + dosageRecordIdToDelete,
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove single session dosage response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 18)
    public void testVerifyRemoveSingleSessionDosageWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosage").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")) + dosageRecordIdToDelete,
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove single session dosage when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 19)
    public void testVerifyRemoveSingleSessionDosageWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosage").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dosageRecordIdToDelete,
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove single session dosage when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 20)
    public void testVerifyRemoveSingleSessionDosageWithInvalidDosagesId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosage").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId) + dataProp.getProperty("INVALID_TEXT"),
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove single session dosage when invalid dosagesId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.drugRecordID[0]"), GeneralErrorMessages.INVALID_VALUE_INT64.value);
    }

    @Test(priority = 21)
    public void testVerifyRemoveSessionDosages() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosages").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dosagesSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 22)
    public void testVerifyRemoveSessionDosagesWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosages").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")), CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove session dosages when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 23)
    public void testVerifyRemoveSessionDosagesWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_RemoveSessionDosages").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test remove session dosages when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
    }

    @Test(priority = 24)
    public void testVerifyAdd25DosagesToSessionDosage() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, getSessionDosagesSessionId()), //Generate new session to add data
                fileReader.getTestJsonFile(testAdd25SessionDosagesData));
        String getDosagesRecordId = response.jsonPath().getString("[0].DosageRecordID");
        Assert.assertFalse(getDosagesRecordId.isEmpty());
        ExtentLogger.pass("Test add 25 session dosages response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 25)
    public void testVerifyAdd25DosagesToSessionDosageWithInvalidSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(testAdd25SessionDosagesData));
        ExtentLogger.pass("Test add 25 session dosages response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 26)
    public void testVerifyAdd25DosagesToSessionDosageWithBlankSessionId() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(testAdd25SessionDosagesData));
        ExtentLogger.pass("Test add 25 session dosages response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 27)
    public void testVerifyAdd25DosagesToSessionDosageWithEmptyBody() {
        sessionDosagesEndpoint = prop.getProperty("Dosages_AddSessionDosages").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(sessionDosagesEndpoint, getSessionDosagesSessionId()), //Generate new session to add data
                CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test add 25 session dosages response when empty body:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.dosages[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getSessionDosagesSessionId() {
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionData.json";
        dosagesSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return dosagesSessionId;
    }
}