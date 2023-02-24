package api.tests.test_suite.plancompare;

import api.base.core.DeleteRequest;
import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;

public class PharmaciesTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    PutRequest putRequest = new PutRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    DeleteRequest deleteRequest = new DeleteRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    String createSessionData;
    private String emptyBodyData;
    private String pharmacySessionId;
    private String postPharmacyId;
    private int pharmacyRecordID;
    private String invalidSessionId;
    private String blankSessionId;
    private String invalidSessionIdErrorCode;
    private String blankSessionIdResponseError;
    private String blankSessionIdMessage;
    private String PharmacyNABPValue;
    private String blankBodyDataMessage;
    private String blankBodyDataErrorMessage;
    private String invalidSessionIdResponse;

    @Test(priority = 1)
    public void testAddSessionPharmacy() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String addSessionToPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addSessionToPharmacyEndpoint, getSessionPharmacySessionId()), fileReader.getTestJsonFile(addSessionToPharmacyData));
        Boolean isMailOrder = Boolean.valueOf(CommonValues.FALSE_VALUE.value);
        String postPharmacyNABPValue = dataProp.getProperty("Post_PharmacyNABP");
        Boolean isMailOrderResponseValue = response.jsonPath().get("[0].IsMailOrder");
        String pharmacyNABPValue = response.jsonPath().get("[0].PharmacyNABP");
        postPharmacyId = response.jsonPath().get("[0].PharmacyID");
        assertEquals(isMailOrderResponseValue, isMailOrder);
        assertEquals(pharmacyNABPValue, postPharmacyNABPValue);
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add session pharmacy response:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testAddSessionPharmacyWithInvalidSessionId() {
        String addSessionToPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        invalidSessionId = GeneralErrorMessages.INVALID_TEXT.value;
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addSessionToPharmacyEndpoint, invalidSessionId), fileReader.getTestJsonFile(addSessionToPharmacyData));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Add Session To Pharmacy With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testAddSessionPharmacyWithBlankSessionId() {
        String addSessionToPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        blankSessionId = dataProp.getProperty("BLANK_SESSION_ID");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addSessionToPharmacyEndpoint, blankSessionId), fileReader.getTestJsonFile(addSessionToPharmacyData));
        blankSessionIdMessage = response.jsonPath().get("Message");
        blankSessionIdResponseError = response.jsonPath().get("ModelState.session[0]");
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Session To Pharmacy With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testAddSessionPharmacyWithBlankBodyData() {
        emptyBodyData = CommonValues.BLANK_BODY_REQUEST.value;
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addSessionToPharmacyEndpoint, pharmacySessionId), emptyBodyData);
        blankBodyDataMessage = response.jsonPath().get("Message");
        blankBodyDataErrorMessage = response.jsonPath().get("ModelState.pharmacies[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Session To Pharmacy With Blank BodyData response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testGetSessionPharmacies() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, pharmacySessionId));
        String getPharmacyId = response.jsonPath().get("[0].PharmacyID");
        String getPharmacyNABP = response.jsonPath().get("[0].PharmacyNABP");
        assertEquals(getPharmacyId, postPharmacyId);
        assertEquals(getPharmacyNABP, dataProp.getProperty("Post_PharmacyNABP"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get session pharmacies response:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testGetSessionPharmaciesWithInvalidSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, invalidSessionId));
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Pharmacies With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testGetSessionPharmaciesWithBlankSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, blankSessionId));
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Pharmacies With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testPutPharmaciesSession() {
        String putPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(putPharmaciesEndpoint, pharmacySessionId), fileReader.getTestJsonFile(putPharmacyData));
        pharmacyRecordID = response.jsonPath().getInt("[0].PharmacyRecordID");
        String putPharmacyNABPValueResponse = response.jsonPath().get("[0].PharmacyNABP");
        Boolean getIsMailOrder = response.jsonPath().get("[0].IsMailOrder");
        PharmacyNABPValue = dataProp.getProperty("Put_PharmacyNABP");
        assertEquals(putPharmacyNABPValueResponse, PharmacyNABPValue);
        assertEquals(getIsMailOrder, Boolean.valueOf(CommonValues.FALSE_VALUE.value));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Put pharmacies to session response:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testPutPharmaciesSessionWithInvalidSessionId() {
        String putPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(putPharmaciesEndpoint, invalidSessionId), fileReader.getTestJsonFile(putPharmacyData));
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Put Pharmacies To Session With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 10)
    public void testPutPharmaciesSessionWithBlankSessionId() {
        String putPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(putPharmaciesEndpoint, blankSessionId), fileReader.getTestJsonFile(putPharmacyData));
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Put Pharmacies To Session With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 11)
    public void testPutPharmaciesSessionWithBlankBodyData() {
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(putPharmaciesEndpoint, pharmacySessionId), emptyBodyData);
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Put Pharmacies To Session With Blank Body Data response:- " + response.asPrettyString());
    }

    @Test(priority = 12)
    public void testGetSessionPharmacy() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionPharmacyEndpoint, pharmacySessionId, pharmacyRecordID));
        int getPharmacyRecordID = response.jsonPath().getInt("PharmacyRecordID");
        assertEquals(getPharmacyRecordID, pharmacyRecordID);
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get session Pharmacy response:- " + response.asPrettyString());
    }

    @Test(priority = 13)
    public void testGetSessionPharmacyWithInvalidSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionPharmacyEndpoint, invalidSessionId, pharmacyRecordID));
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get session Pharmacy With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 14)
    public void testGetSessionPharmacyWithBlankSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionPharmacyEndpoint, blankSessionId, pharmacyRecordID));
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get session Pharmacy With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 15)
    public void testPostPharmacySessionModify() {
        String postPharmacyToSessionModifyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postPharmacyToSessionModifyEndpoint, pharmacySessionId), fileReader.getTestJsonFile(postPharmacyToSessionModifyData));
        String postPharmacyModifyNABP = response.jsonPath().get("[0].PharmacyNABP");
        assertEquals(postPharmacyModifyNABP, PharmacyNABPValue);
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Post Pharmacy to session modify response:- " + response.asPrettyString());
    }

    @Test(priority = 16)
    public void testPostPharmacySessionModifyWithInvalidSessionId() {
        String postPharmacyToSessionModifyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postPharmacyToSessionModifyEndpoint, invalidSessionId), fileReader.getTestJsonFile(postPharmacyToSessionModifyData));
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Post Pharmacy to session modify With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 17)
    public void testPostPharmacySessionModifyWithBlankSessionId() {
        String postPharmacyToSessionModifyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postPharmacyToSessionModifyEndpoint, blankSessionId), fileReader.getTestJsonFile(postPharmacyToSessionModifyData));
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Post Pharmacy to session modify With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 18)
    public void testPostPharmacySessionModifyWithBlankBodyData() {
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addSessionToPharmacyEndpoint, pharmacySessionId), emptyBodyData);
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Post Pharmacy To Session Modify With Blank Body Data response:- " + response.asPrettyString());
    }

    @Test(priority = 19)
    public void testDeletePharmacySession() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deletePharmacyToSessionEndpoint, pharmacySessionId, pharmacyRecordID), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 204);
        ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
    }

    @Test(priority = 20)
    public void testDeletePharmacySessionWithInvalidSessionId() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deletePharmacyToSessionEndpoint, invalidSessionId, pharmacyRecordID), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
    }

    @Test(priority = 21)
    public void testDeletePharmacySessionWithBlankSessionId() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(deletePharmacyToSessionEndpoint, blankSessionId, pharmacyRecordID), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
    }

    @Test(priority = 22)
    public void testDeletePharmaciesSession() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deletePharmaciesTosSessionEndpoint, pharmacySessionId), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 204);
        ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
    }

    @Test(priority = 23)
    public void testDeletePharmaciesSessionWithInvalidSessionId() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(deletePharmaciesTosSessionEndpoint, invalidSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
    }

    @Test(priority = 24)
    public void testDeletePharmaciesSessionWithBlankSessionId() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(deletePharmaciesTosSessionEndpoint, blankSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        String blankSessionIdMessageDeleteRequest = response.jsonPath().get("Message");
        assertEquals(blankSessionIdMessageDeleteRequest, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
    }

    private String getSessionPharmacySessionId() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionPharmacyData.json";
        pharmacySessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return pharmacySessionId;
    }
}