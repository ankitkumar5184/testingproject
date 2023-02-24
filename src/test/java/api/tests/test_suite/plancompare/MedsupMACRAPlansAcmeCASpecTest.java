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
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.*;

public class MedsupMACRAPlansAcmeCASpecTest {

    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonMethods commonMethods = new CommonMethods();
    FileReader fileReader = new FileReader();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    String testMedsupSubmitAnswersACMEData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/medsupSubmitAnswersACME_MACRA_old_Bprior2020_Aafter2020.json";

    protected String medsupMACRAPlansAcmeCASessionId;
    protected String medsupMACRAPlansAcmeCAEndpoint;
    protected String newlyEligibleQuestionId = "23";
    boolean newlyEligibleQ23Answer;

    @Test(priority = 1)
    public void testVerifyGetSessionRequest() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, medsupMACRAPlansAcmeCASessionId()));
        ExtentLogger.pass("Test get session request response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionRequestWithInvalidSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("INVALID_TEXT")));
        ExtentLogger.pass("Test get session request response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionRequestWithBlankSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test get session request response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifySearchAvailablePlansForSession() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, medsupMACRAPlansAcmeCASessionId) + dataProp.getProperty("GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNull(response.jsonPath().getString("MedigapPlans"));
    }

    @Test(priority = 5)
    public void testVerifySearchAvailablePlansForSessionWithInvalidSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("INVALID_TEXT")) + dataProp.getProperty("GetPlansData"));
        ExtentLogger.pass("Test get search available plans for session response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifySearchAvailablePlansForSessionWithBlankSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansData"));
        ExtentLogger.pass("Test get search available plans for session response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetQuestions() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_AddAnswers").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, medsupMACRAPlansAcmeCASessionId) + dataProp.getProperty("MedsupMACRAPlansAcmeCA_GetQuestionsData"));
        ExtentLogger.pass("Test get questions response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("[10].QuestionID"), dataProp.getProperty("dobQuestionId"));
        Assert.assertEquals(response.jsonPath().getString("[11].QuestionID"), dataProp.getProperty("partBEffectiveDateQuestionId"));
        Assert.assertEquals(response.jsonPath().getString("[12].QuestionID"), dataProp.getProperty("partAEffectiveDateQuestionId"));
    }

    @Test(priority = 8)
    public void testVerifyGetQuestionsWithInvalidSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_AddAnswers").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("INVALID_TEXT")) + dataProp.getProperty("MedsupMACRAPlansAcmeCA_GetQuestionsData"));
        ExtentLogger.pass("Test get questions response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetQuestionsWithBlankSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("MedsupMACRAPlansAcmeCA_AddAnswers").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedsupMACRAPlansAcmeCA_GetQuestionsData"));
        ExtentLogger.pass("Test get questions response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testVerifySubmitAnswers() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, medsupMACRAPlansAcmeCASessionId),
                fileReader.getTestJsonFile(testMedsupSubmitAnswersACMEData));
        ExtentLogger.pass("Test submit answers response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.asPrettyString(), "null");
    }

    @Test(priority = 9)
    public void testVerifySubmitAnswersWithInvalidSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(testMedsupSubmitAnswersACMEData));
        ExtentLogger.pass("Test submit answers response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 10)
    public void testVerifySubmitAnswersWithBlankSessionId() {
        medsupMACRAPlansAcmeCAEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(medsupMACRAPlansAcmeCAEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(testMedsupSubmitAnswersACMEData));
        ExtentLogger.pass("Test submit answers response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 11)
    public void testVerifyGetCreatedSession() {
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + medsupMACRAPlansAcmeCASessionId);
        ExtentLogger.pass("Test get created session response:- " + response.asPrettyString());
        Assert.assertEquals(response.jsonPath().getString("Questions[12].QuestionID"), newlyEligibleQuestionId);

        if (response.jsonPath().getString("Questions[12].QuestionID").equals(newlyEligibleQuestionId)) {
            String newlyEligibleQ23AnswerString = response.jsonPath().getString("Questions[12].Value");
            if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Yes")) {
                newlyEligibleQ23Answer = true;
            } else if (newlyEligibleQ23AnswerString.equalsIgnoreCase("No")) {
                newlyEligibleQ23Answer = false;
            } else {
               throw new RuntimeException("Could not parse: " + newlyEligibleQ23AnswerString);
            }
        }
    }

    @Test(priority = 12)
    public void testVerifyGetCreatedSessionWithInvalidSessionId() {
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + dataProp.getProperty("INVALID_TEXT"));
        ExtentLogger.pass("Test get created session response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 13)
    public void testVerifyGetCreatedSessionWithBlankSessionId() {
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + dataProp.getProperty("BLANK_SESSION_ID"));
        ExtentLogger.pass("Test get created session response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    private String medsupMACRAPlansAcmeCASessionId() {
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA.json";
        medsupMACRAPlansAcmeCASessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return medsupMACRAPlansAcmeCASessionId;
    }
}