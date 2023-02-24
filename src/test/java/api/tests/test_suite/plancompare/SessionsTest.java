package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class SessionsTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();
    private static String getCreateSessionSSOValue;
    private  String invalidErrorMessage;
    private String invalidErrorCode;
    private String getSessionId;
    private String postSessionEditData;
    private String getPostSessionEditEndPoint;

    @Test
    public void testVerifyCreateSession() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String postCreateSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionApiData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionData));
        getSessionId = response.jsonPath().getString("[0].SessionID");
        String getReqId = response.jsonPath().getString("[0].reqid");
        String getStatus = response.jsonPath().getString("[0].Status");
        getCreateSessionSSOValue = response.jsonPath().getString("[0].SSOValue");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 1)
    public void testVerifyCreateSessionWithBlankBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                CommonValues.BLANK_BODY_REQUEST.value);
        String emptyBodyResponseMessage = response.jsonPath().getString("Message");
        String emptyBodyErrorMessage = response.jsonPath().getString("ModelState.sessions[0]");
        assertEquals(emptyBodyResponseMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(emptyBodyErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testVerifyGetSession(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, getSessionId));
        String getSSOValue = response.jsonPath().getString("SSOValue");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(getSSOValue, getCreateSessionSSOValue);
        ExtentLogger.pass("Response :- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithInvalidSessionId(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, dataProp.getProperty("INVALID_TEXT")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session with invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testVerifyGetSessionWithBlankSessionId(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Get Session with blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testVerifyPostSessionEdit() {
        postSessionEditData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/postSessionEditApiData.json";
        getPostSessionEditEndPoint = prop.getProperty("Sessions_PostSessionEdit").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, getSessionId),
                fileReader.getTestJsonFile(postSessionEditData));
        String getZipCode = response.jsonPath().getString("Zip");
        String getFipsValue = response.jsonPath().getString("Fips");
        String getBirthDate = response.jsonPath().getString("Birthdate");
        assertEquals(getZipCode, ZipCodeValues.Zip_90011.value);
        assertEquals(getFipsValue, FipsValues.Fips_06037.value);
        assertEquals(getBirthDate, dataProp.getProperty("BIRTHDATE"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testVerifyPostSessionEditWithBlankSessionId(){
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(postSessionEditData));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ModelState['sessions.Zip'][0]");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Post Session Edit with SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testVerifyPostSessionEditWithInvalidSessionId(){
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(postSessionEditData));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Post Session Edit with invalid SessionId response:- " + response.asPrettyString());
    }
}