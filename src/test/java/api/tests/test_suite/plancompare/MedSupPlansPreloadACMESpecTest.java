package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MedSupPlansPreloadACMESpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    String postCreateSessionWithLoadedQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCAWithQuestions.json";
    String postCreateSessionWithoutLoadedQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA.json";

    private String medSupPlansPreloadACMESessionId;
    private String medSupPlansPreloadACMESpecSessionEndpoint;
    protected String newlyEligibleQuestionId = "23";
    boolean newlyEligibleQ23AnswerActual;
    private String getSessionEndpoint;
    private String getPlanId;

    @Test
    public void testVerifyGetCreatedSessionWithLoadedQuestions() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + getCreateSession(postCreateSessionWithLoadedQuestionsData));
        ExtentLogger.pass("Test get created session response:- " + response.asPrettyString());
        assertEquals(response.jsonPath().getString("Questions[18].QuestionID"), newlyEligibleQuestionId);
        if (response.jsonPath().getString("Questions[18].QuestionID").equals(newlyEligibleQuestionId)) {
            String newlyEligibleQ23AnswerString = response.jsonPath().getString("Questions[18].Value");
            if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Yes")) {
                newlyEligibleQ23AnswerActual = true;
            } else if (newlyEligibleQ23AnswerString.equalsIgnoreCase("No")) {
                newlyEligibleQ23AnswerActual = false;
            } else {
                throw new RuntimeException("Could not parse: " + newlyEligibleQ23AnswerString);
            }
            boolean newlyEligibleQ23AnswerExpected = true;
            assert newlyEligibleQ23AnswerActual == newlyEligibleQ23AnswerExpected;
        }
    }

    @Test(priority = 1)
    public void testVerifyGetSessionWithLoadedQuestionsInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("GetSessionWithInvalidSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithLoadedQuestionsBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("GetSessionWithBlankSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 3)
    public void testVerifySearchAvailablePlansForSession(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId) +
                        String.format(dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"))));
        ExtentLogger.pass("SearchAvailablePlansForSession response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedigapPlans"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].ID"));
        assertEquals(response.jsonPath().getString("MedigapPlans[0].PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 4)
    public void testVerifySearchAvailablePlansForSessionWithBlankSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        ExtentLogger.pass("SearchAvailablePlansForSessionWithBlankSessionID response :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 5)
    public void testVerifySearchAvailablePlansForSessionWithInvalidSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        ExtentLogger.pass("SearchAvailablePlansForSessionWithInvalidSessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetCreatedSessionWithoutLoadedQuestions() {
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + getCreateSession(postCreateSessionWithoutLoadedQuestionsData));
        ExtentLogger.pass("GetCreatedSessionWithoutLoadedQuestions:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 7)
    public void testVerifyGetSessionWithoutLoadedQuestionsInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("GetSessionWithInvalidSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testVerifyGetSessionWithoutLoadedQuestionsBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("GetSessionWithBlankSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 9)
    public void testVerifyUpdateSessionWithQuestions(){
        String updateSessionCAWithQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionEndpoint, medSupPlansPreloadACMESessionId), fileReader.getTestJsonFile(updateSessionCAWithQuestionsData));
    ExtentLogger.pass("UpdateSessionWithQuestions :- " + response.asPrettyString());
    assertEquals(response.getStatusCode(), 200);
    assertEquals(response.jsonPath().getString("Birthdate").replace("T00:00:00", ""), dataProp.getProperty("BIRTH_DATE_UPDATE_SESSION"));
    assertEquals(response.jsonPath().getString("HealthStatus"), dataProp.getProperty("HEALTH_STATUS"));
    }

    @Test(priority = 10)
    public void testVerifyUpdateSessionWithQuestionsWithInvalidSessionID(){
        String updateSessionCAWithQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("INVALID_TEXT")),fileReader.getTestJsonFile(updateSessionCAWithQuestionsData));
        ExtentLogger.pass("UpdateSessionWithQuestions :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 11)
    public void testVerifyUpdateSessionWithQuestionsWithBlankSessionID(){
        String updateSessionCAWithQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(updateSessionCAWithQuestionsData));
        ExtentLogger.pass("UpdateSessionWithQuestions :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState['sessions.Birthdate'][0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 12)
    public void testVerifyGetCreatedSessionAfterUpdatingSessionWithQuestions() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, medSupPlansPreloadACMESessionId));
        ExtentLogger.pass("GetSession response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("Questions[18].QuestionID"), newlyEligibleQuestionId);
        if (response.jsonPath().getString("Questions[18].QuestionID").equals(newlyEligibleQuestionId)) {
            String newlyEligibleQ23AnswerString = response.jsonPath().getString("Questions[18].Value");
            if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Yes")) {
                newlyEligibleQ23AnswerActual = true;
            } else if (newlyEligibleQ23AnswerString.equalsIgnoreCase("No")) {
                newlyEligibleQ23AnswerActual = false;
            } else {
                throw new RuntimeException("Could not parse: " + newlyEligibleQ23AnswerString);
            }
            boolean newlyEligibleQ23AnswerExpected = true;
            assert newlyEligibleQ23AnswerActual == newlyEligibleQ23AnswerExpected;
        }
    }

    @Test(priority = 13)
    public void testVerifyGetCreatedSessionAfterUpdatingSessionWithQuestionsWithInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("GetSessionWithInvalidSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 14)
    public void testVerifyCreatedGetSessionAfterUpdatingSessionWithQuestionsWithBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("GetSessionWithBlankSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 15)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestions(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId) +
                        String.format(dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"))));
        ExtentLogger.pass("SearchAvailablePlansAfterUpdatingSessionWithQuestions response:- " + response.asPrettyString());
        getPlanId = response.jsonPath().getString("MedigapPlans[0].ID");
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedigapPlans"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].ID"));
        assertEquals(response.jsonPath().getString("MedigapPlans[0].PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 16)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestionsWithBlankSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        ExtentLogger.pass("SearchAvailablePlansAfterUpdatingSessionWithQuestionsWithBlankSessionID response :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 17)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestionsWithInvalidSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        ExtentLogger.pass("SearchAvailablePlansAfterUpdatingSessionWithQuestionsWithInvalidSessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 18)
    public void testVerifyGetPlanDetails() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId, getPlanId) + dataProp.getProperty
                ("MedSupPlansPreloadACMESpec_GetPlansDetails"));
        ExtentLogger.pass("Test get plan details for Medicare plan response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("PlanDataFields[0].Type"), dataProp.getProperty("Type"));
        assertEquals(response.jsonPath().getString("PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("CarrierName"));
        assertNotNull(response.jsonPath().getString("AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 19)
    public void testVerifyGetPlanDetailsWithInvalidSessionId() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint,
                GeneralErrorMessages.INVALID_TEXT.value, getPlanId) + dataProp.getProperty("MedSupPlansPreloadACMESpec_GetPlansDetails"));
        ExtentLogger.pass("PlanDetailsWithInvalidSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 20)
    public void testVerifyGetPlanDetailsForWithBlankSessionId() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint,
                dataProp.getProperty("BLANK_SESSION_ID"), getPlanId) + dataProp.getProperty("MedSupPlansPreloadACMESpec_GetPlansDetails"));
        ExtentLogger.pass("PlanDetailsForWithBlankSessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    private String getCreateSession(String filePath) {
        medSupPlansPreloadACMESessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(filePath));
        return medSupPlansPreloadACMESessionId;
    }
}