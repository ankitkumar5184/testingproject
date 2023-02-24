package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testng.Assert.assertEquals;

public class MedSupPlansSMSSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    String medSupPlansSMSSpecSessionId;
    String getMedGapId;

    @Test(priority = 1)
    public void testGetSession() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getMedSupPlansSMSSpecSessionId()));
        ExtentLogger.pass("Test get session response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("Test get session with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test Get Session With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testGetPlansBeforeQuestions() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, medSupPlansSMSSpecSessionId)
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testGetPlansBeforeQuestionsWithInvalidSessionId() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        ExtentLogger.pass("Test Get Plans Before Questions With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testGetPlansBeforeQuestionsWithBlankSessionId() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        ExtentLogger.pass("Test Get Plans Before Questions With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testGetQuestions() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, medSupPlansSMSSpecSessionId) + dataProp.getProperty("MedSupPlansSMSS_Questions"));
        ExtentLogger.pass("Test Get Questions response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testGetQuestionsWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansSMSS_Questions"));
        ExtentLogger.pass("Test Get Questions With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testGetQuestionsWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansSMSS_Questions"));
        ExtentLogger.pass("Test Get Questions With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }


    @Test(priority = 10)
    public void testSubmitAnswers() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansSMSS_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/medSupSubmitAnswersSMS.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, medSupPlansSMSSpecSessionId), fileReader.getTestJsonFile(getSubmitAnswersData));
        ExtentLogger.pass("Test Submit Answers response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testSubmitAnswersWithInvalidSessionId() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansSMSS_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/medsupSubmitAnswersCA.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getSubmitAnswersData));
        ExtentLogger.pass("Test Submit Answers With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testSubmitAnswersWithBlankSessionId() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansSMSS_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/medsupSubmitAnswersCA.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(getSubmitAnswersData));
        ExtentLogger.pass("Test Submit Answers With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 13)
    public void testSubmitAnswersWithBlankBodyData() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansSMSS_Submit_Answers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test Submit Answers With Blank Body Data response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }


    @Test(priority = 14)
    public void testReviewSession() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, medSupPlansSMSSpecSessionId));
        ExtentLogger.pass("Test get Session Review response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 15)
    public void testReviewSessionWithInvalidSessionId() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("Test Get Session Review with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 16)
    public void testReviewSessionWithBlankSessionId() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansSMSS_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test Get Session Review With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 17)
    public void testGetMedGapPlans() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, medSupPlansSMSSpecSessionId)
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        assertEquals(response.getStatusCode(), 200);
        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("Humana"));
        getMedGapId = response.jsonPath().getString("MedicarePlans[0].ID");
    }

    @Test(priority = 18)
    public void testGetMedGapPlansWithInvalidSessionId() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        ExtentLogger.pass("Test Get MedGap Plans With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 19)
    public void testGetMedGapPlansWithBlankSessionId() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansSMSS_GetPlans_Data"));
        ExtentLogger.pass("Test Get MedGap Plans With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 20)
    public void testCheckPlanDetailsForEachMedGapPlan(){
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, medSupPlansSMSSpecSessionId) + dataProp.getProperty("MedSupPlansSMSS_GetPlan_Data"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 21)
    public void testCheckPlanDetailsForEachMedGapPlanWithInvalidSessionId() {
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MedSupPlansSMSS_GetPlan_Data"));
        ExtentLogger.pass("Test Check Plan Details For Each MedGapPlan With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 22)
    public void testCheckPlanDetailsForEachMedGapPlanWithBlankSessionId() {
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansSMSS_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansSMSS_GetPlan_Data"));
        ExtentLogger.pass("Test Check Plan Details For Each MedGap Plan With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 23)
    public void testEnroll() {
        String getEnrollEndPoint = prop.getProperty("MedSupPlansSMSS_Enroll").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollEndPoint, medSupPlansSMSSpecSessionId) + "PlanPremium=204.75", fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Get Enroll response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 24)
    public void testEnrollWithInvalidSessionId() {
        String getEnrollEndPoint = prop.getProperty("MedSupPlansSMSS_Enroll").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettingNoBroker.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollEndPoint, GeneralErrorMessages.INVALID_TEXT.value) + "PlanPremium=204.75", fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Get Enroll With Invalid Session Id response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 25)
    public void testEnrollWithBlankSessionId() {
        String getEnrollEndPoint = prop.getProperty("MedSupPlansSMSS_Enroll").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettingNoBroker.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollEndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + "PlanPremium=204.75", fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Get Enroll With Blank Session Id response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 26)
    public void testEnrollWithBlankBodyData() {
        String getEnrollEndPoint = prop.getProperty("MedSupPlansSMSS_Enroll").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollEndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + "PlanPremium=204.75", CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        ExtentLogger.pass("Test Get Enroll Url With Blank Body Data response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    private String getMedSupPlansSMSSpecSessionId() {
        String postCreateSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionSMS.json";
        medSupPlansSMSSpecSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return medSupPlansSMSSpecSessionId;
    }
}