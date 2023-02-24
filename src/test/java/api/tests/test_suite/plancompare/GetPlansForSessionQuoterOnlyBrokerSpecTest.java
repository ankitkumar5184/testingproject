package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.PlanCompareErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class GetPlansForSessionQuoterOnlyBrokerSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String createSessionData;
    private String memberSearchSessionId;
    private String getSessionPlansBrokerSpecEndPoint;
    private String postSessionPlansBrokerSpecEndPoint;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String forbiddenPlanIdResponse;
    private String forbiddenPlanIdErrorCode;

    @Test(priority = 1)
    public void testCheckLimitedPlanTypesInCaForBrokerCase() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId()) + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testCheckLimitedPlanTypesInCaForBrokerCaseInvalidSession() {
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testCheckLimitedPlanTypesInCaForBrokerCaseBlankSession() {
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testEnrollForCaForbiddenPlanType1() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId()), CommonValues.BLANK_BODY_REQUEST.value);
        forbiddenPlanIdResponse = response.jsonPath().get("Message");
        forbiddenPlanIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(forbiddenPlanIdResponse, PlanCompareErrorMessages.COULD_NOT_FIND_MAPPINGS_FOR_ENROLLMENT.value);
        assertEquals(forbiddenPlanIdErrorCode, PlanCompareErrorMessages.NFEN_ROLL.value);
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testEnrollForCaForbiddenPlanType1InvalidSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value), CommonValues.BLANK_BODY_REQUEST.value);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testEnrollForCaForbiddenPlanType1BlankSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testEnrollForCaForbiddenPlanType2() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId()), CommonValues.BLANK_BODY_REQUEST.value);
        forbiddenPlanIdResponse = response.jsonPath().get("Message");
        forbiddenPlanIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(forbiddenPlanIdResponse, PlanCompareErrorMessages.COULD_NOT_FIND_MAPPINGS_FOR_ENROLLMENT.value);
        assertEquals(forbiddenPlanIdErrorCode, PlanCompareErrorMessages.NFEN_ROLL.value);
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testEnrollForCaForbiddenPlanType2InvalidSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value), CommonValues.BLANK_BODY_REQUEST.value);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testEnrollForCaForbiddenPlanType2BlankSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
    }

    private String getSessionPlansBrokerSpecId() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionMinimalBrokerTestUser12793Data.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), "[ " + fileReader.getTestJsonFile(createSessionData) + " ]");
        return memberSearchSessionId;
    }
}