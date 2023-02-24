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
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class QuestionsTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    private String questionsSessionId;
    private String invalidQuestionSessionId;
    private String blankQuestionsSessionId;
    private String blankBody;

    @Test(priority = 1)
    public void testGetQuestions() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, getQuestionsSessionId()) + dataProp.getProperty("Questions_GetQuestionsData"));
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get questions response:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testGetQuestionsWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        invalidQuestionSessionId = dataProp.getProperty("INVALID_TEXT");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsData"));
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Get Questions With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testGetQuestionsWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        blankQuestionsSessionId = dataProp.getProperty("BLANK_SESSION_ID");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsData"));
        String blankErrorMessage = response.jsonPath().getString("Message");
        String blankErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(blankErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Get Questions With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testAddAnswers() {
        String addAnswersToQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Answers response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testAddAnswersWithInvalidSessionId() {
        String addAnswersToQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, invalidQuestionSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Add Answers With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testAddAnswersWithBlankSessionId() {
        String addAnswersToQuestionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, blankQuestionsSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Answers With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testAddAnswersWithBlankBodyData() {
        blankBody = CommonValues.BLANK_BODY_REQUEST.value;
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), blankBody);
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Answers With Blank Body Data response:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testGetDentalQuestions() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Dental Questions response:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testGetDentalQuestionsWithInvalidSessionId() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Get Dental Questions With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 10)
    public void testGetDentalQuestionsWithBlankSessionId() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        String blankSessionIdErrorMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Dental Questions With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 11)
    public void testAddDentalVisionAnswers() {
        String addDentalVisionAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Dental Vision Answers response:- " + response.asPrettyString());
    }

    @Test(priority = 12)
    public void testAddDentalVisionAnswersWithInvalidSessionId() {
        String addDentalVisionAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, invalidQuestionSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        String invalidSessionIdMessage = response.jsonPath().getString("Message");
        String invalidSessionIdErrorMessage = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidSessionIdMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorMessage, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Add Dental Vision Answers With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 13)
    public void testAddDentalVisionAnswersWithBlankSessionId() {
        String addDentalVisionAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, blankQuestionsSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Dental Vision Answers With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 14)
    public void testAddDentalVisionAnswersWithBlankBodyData() {
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), blankBody);
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Dental Vision Answers With Blank SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 15)
    public void testGetQuestionsALT() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        assertEquals(response.getStatusCode(), 200);
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
        ExtentLogger.pass("Get Question ALT response:- " + response.asPrettyString());
    }

    @Test(priority = 16)
    public void testGetQuestionsALTWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Question ALT With Invalid SessionId response:- " + response.asPrettyString());
    }

    @Test(priority = 17)
    public void testGetQuestionsALTWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        String blankSessionIdErrorMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Question ALT With Blank SessionId response:- " + response.asPrettyString());
    }

    private String getQuestionsSessionId() {
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionData.json";
        questionsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return questionsSessionId;
    }
}