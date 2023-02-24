package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonDateTimeMethods;
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
import static org.testng.Assert.assertEquals;

public class MedicarePlansHumana2020SpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();
    private String medicarePlansHumana2020SessionId;
    String medicarePlansId;

    @Test(priority = 1)
    public void testVerifyGetSession() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("MedicarePlansHumana2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getMedicarePlansHumana2020SessionId()));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get session response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("MedicarePlansHumana2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get session with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("MedicarePlansHumana2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get session with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, medicarePlansHumana2020SessionId) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getNetworkType = response.jsonPath().getString("MedicarePlans[0].PlanDataFields[0].Type");
        Assert.assertEquals(getNetworkType, dataProp.getProperty("Type"));
        Assert.assertEquals(response.jsonPath().getInt("MedicarePlans[0].PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("MedicarePlans[0].PlanType").isEmpty());
        Assert.assertTrue(response.jsonPath().getString("MedicarePlans").length() > 2);
        medicarePlansId = response.jsonPath().getString("MedicarePlans[0].ID");
    }

    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansData"));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get plans with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithBlankSessionId() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansData"));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get plans with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetails() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlansDetails").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, medicarePlansHumana2020SessionId, medicarePlansId) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansDetailsData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getNetworkType = response.jsonPath().getString("PlanDataFields[0].Type");
        Assert.assertEquals(getNetworkType, dataProp.getProperty("Type"));
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsWithInvalidSessionId() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlansDetails").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, GeneralErrorMessages.INVALID_TEXT.value, medicarePlansId) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansDetailsData"));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details for medicare plan with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsForWithBlankSessionId() {
        String medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansHumana2020_GetPlansDetails").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansId) + dataProp.getProperty("MedicarePlansHumana2020_GetPlansDetailsData"));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details for medicare plan with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifyGetEnrollUrl() {
        String getEnrollUrlData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansHumana2020_GetEnrollUrl").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, medicarePlansHumana2020SessionId, medicarePlansId),
                fileReader.getTestJsonFile(getEnrollUrlData));
        ExtentLogger.pass("Test get enroll Url response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("Url"));
        Assert.assertFalse(response.jsonPath().getString("EnrollActionType").isEmpty());
    }

    @Test(priority = 14)
    public void testVerifyGetEnrollUrlWithInvalidSessionId() {
        String getEnrollUrlData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansHumana2020_GetEnrollUrl").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, GeneralErrorMessages.INVALID_TEXT.value, medicarePlansId),
                fileReader.getTestJsonFile(getEnrollUrlData));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifyGetEnrollUrlWithBlankSessionId() {
        String getEnrollUrlData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansHumana2020_GetEnrollUrl").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansId),
                fileReader.getTestJsonFile(getEnrollUrlData));
        ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 16)
    public void testVerifyGetEnrollUrlWithBlankBodyData() {
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansHumana2020_GetEnrollUrl").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                + String.format(getEnrollUrlEndpoint, medicarePlansHumana2020SessionId, medicarePlansId), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with blank body data response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getMedicarePlansHumana2020SessionId() {
        medicarePlansHumana2020SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return medicarePlansHumana2020SessionId;
    }
}