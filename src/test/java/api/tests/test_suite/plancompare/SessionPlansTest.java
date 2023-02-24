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
import static org.testng.Assert.*;

public class SessionPlansTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();
    private String sessionPlansSmartSessionId;
    private String medicarePlansID;
    private static String planSessionId;
    private String getPlansEndpoint;
    private String invalidErrorMessage;
    private String invalidErrorCode;
    private String getPlanYear;

    @Test
    public void testVerifyCreateSessionPlanSmart() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String postCreateSessionForPlanSmartData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionPlanSmartData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Plans_PlanSmartCreateSession"),
                        fileReader.getTestJsonFile(postCreateSessionForPlanSmartData));
        sessionPlansSmartSessionId = response.jsonPath().getString("[0].SessionID");
        String getSSoValue = response.jsonPath().getString("[0].SSOValue");
        String getStatusUpdate = response.jsonPath().getString("[0].Status");
        assertEquals(getSSoValue, dataProp.getProperty("CREATE_PLAN_SMART_SSO_VALUE"));
        assertEquals(getStatusUpdate, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 1)
    public void testVerifyCreateSessionPlanSmartWithBlankBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Plans_PlanSmartCreateSession"),
                CommonValues.BLANK_BODY_REQUEST.value);
        String emptyBodyResponseMessage = response.jsonPath().getString("Message");
        String emptyBodyErrorMessage = response.jsonPath().getString("ModelState.sessions[0]");
        assertEquals(emptyBodyResponseMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(emptyBodyErrorMessage,GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testVerifyPlanSmartGetPlans(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, sessionPlansSmartSessionId));
        String getContractID = response.jsonPath().getString("MedicarePlans[0].ContractID");
        String getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getContractID.isEmpty());
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create session response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testVerifyPlanSmartGetPlansWithInvalidSessionId(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("INVALID_TEXT")));
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("PlanSmart Get Plans With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testVerifyPlanSmartGetPlansWithBlankSessionId(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("PlanSmart Get Plans With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testVerifyGetPlans(){
        getPlansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, getSessionPlansSessionId()));
        medicarePlansID = response.jsonPath().getString("MedicarePlans[0].ID");
        String getContractID = response.jsonPath().getString("MedicarePlans[0].ContractID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getContractID.isEmpty());
        assertFalse(medicarePlansID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create session response:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithInvalidSessionId(){
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, dataProp.getProperty("INVALID_TEXT")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Plans With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testVerifyGetPlansWithBlankSessionId(){
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint,  dataProp.getProperty("BLANK_SESSION_ID")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Plans With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testVerifyPostPlansEnroll(){
        String postPlansEnrollmentBodyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, planSessionId, medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        String getUrl = response.jsonPath().getString("Url");
        String getEnrollActionType = response.jsonPath().getString("EnrollActionType");
        String getMethod = response.jsonPath().getString("Method");
        assertFalse(getUrl.isEmpty());
        assertEquals(getEnrollActionType, dataProp.getProperty("ENROLL_ACTION_TYPE"));
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create session response:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testVerifyPostPlansEnrollWithInvalidMedicarePlanId(){
        String postPlansEnrollmentBodyData =System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, getSessionPlansSessionId(), dataProp.getProperty("INVALID_TEXT")), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        String invalidPlanIdErrorMessage = response.jsonPath().getString("Message");
        assertEquals(invalidPlanIdErrorMessage, PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Post Plans Enroll With Invalid Medicare PlanId response" + response.asPrettyString());
    }

    @Test(priority = 10)
    public void testVerifyPostPlansEnrollWithInvalidSessionId(){
        String postPlansEnrollmentBodyData =System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, dataProp.getProperty("INVALID_TEXT"), medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Post Plans Enroll With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 11)
    public void testVerifyPostPlansEnrollWithBlankBody(){
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, planSessionId, medicarePlansID), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ModelState.enrollmentSettings[0]");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(invalidErrorCode,GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Post Plans Enroll With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 12)
    public void testVerifyPostPlansEnrollWithBlankSessionId(){
        String postPlansEnrollmentBodyData =System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Post Plans Enroll With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 13)
    public void testVerifyPostPlansEnrollWithBlankMedicarePlanId(){
        String postPlansEnrollmentBodyData =System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, getSessionPlansSessionId(), dataProp.getProperty("BLANK_MEDICARE_PLAN_ID")), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        String blankPlanIdErrorMessage = response.jsonPath().getString("Message");
        assertEquals(blankPlanIdErrorMessage, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_POST.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Post Plans Enroll With Blank Medicare PlanId response" + response.asPrettyString());
    }

    @Test(priority = 14)
    public void testVerifyGetPlansCount(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, planSessionId) +
                dataProp.getProperty("Plans_PlanCountData"));
        String getPlanType = response.jsonPath().getString("[0].PlanType");
        String getPlanType256Count = response.jsonPath().getString("[0].Count");
        assertEquals(getPlanType, dataProp.getProperty("PLAN_TYPE"));
        assertNotNull(getPlanType256Count);
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Create session response:- " + response.asPrettyString());
    }

    @Test(priority = 15)
    public void testVerifyGetPlansCountWithInvalidSessionId(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("Plans_PlanCountData"));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Plans With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 16)
    public void testVerifyGetPlansCountWithBlankSessionId(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("Plans_PlanCountData"));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Plans With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 17)
    public void testVerifyGetPlanDetails(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId, medicarePlansID));
        String getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details response:- " + response.asPrettyString());
    }

    @Test(priority = 18)
    public void testVerifyGetPlanDetailsWithBlankSessionID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"),
                medicarePlansID));
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Plan Details with blank session ID response:- " + response.asPrettyString());
    }

    @Test(priority = 19)
    public void testVerifyGetPlanDetailsWithInvalidSessionID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("INVALID_TEXT"),
                        medicarePlansID));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Plan Details with invalid session ID response:- " + response.asPrettyString());
    }

    @Test(priority = 20)
    public void testVerifyGetPlanDetailsWithInvalidPlanID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId,
                dataProp.getProperty("INVALID_TEXT")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Get Plan Details with invalid session ID response:- " + response.asPrettyString());
    }

    private String getSessionPlansSessionId(){
        String postCreateSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionData.json";
        planSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return planSessionId;
    }
}