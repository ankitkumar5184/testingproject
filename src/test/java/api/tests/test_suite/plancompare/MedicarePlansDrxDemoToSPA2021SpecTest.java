package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;

public class MedicarePlansDrxDemoToSPA2021SpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    String sessionId;

    @Test(priority = 1)
    public void testEnrollIn2020PCAPIPlansUsingBEAN_DefaultDestinationSiteID() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String enrollIn2020PCAPIPlansUsingBEANEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_DefaultDestinationSiteID").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2020PCAPIPlansUsingBEANEndPoint, getSessionMedicarePlansDrxDemoToSPA2021SpecId()));
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollIn2020PCAPIPlansUsingBEANEndGetPlansPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlans").replace("{SessionID}", "%s");
        Response getPlans = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2020PCAPIPlansUsingBEANEndGetPlansPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getPlanID = getPlans.jsonPath().getString("MedicarePlans[0].ID");

        String enrollIn2020PCAPIPlansUsingBEANEndPlansEnrollPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Enroll").replace("{SessionID}", "%s")
                .replace("{planId}", getPlanID);
        String getPlansEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response getPlansEnroll = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(enrollIn2020PCAPIPlansUsingBEANEndPlansEnrollPoint, sessionId), fileReader.getTestJsonFile(getPlansEnrollData));
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testEnrollIn2021PCAPIPlansUsingBEAN_DefaultDestinationSiteID() {
        String enrollIn2021PCAPIPlansUsingBEANEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_DefaultDestinationSiteID").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANEndPoint, getSessionMedicarePlansDrxDemoToSPA2021SpecId()));
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlans").replace("{SessionID}", "%s");
        Response getPlans = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getPlanID = getPlans.jsonPath().getString("MedicarePlans[1].ID");

        String enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Enroll").replace("{SessionID}", "%s")
                .replace("{planId}", getPlanID);
        String getPlansEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response getPlansEnroll = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint, sessionId), fileReader.getTestJsonFile(getPlansEnrollData));
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testEnrollIn2021PCAPIPlansUsingBEAN_DefaultDestinationSiteIDForSTAGE() {
        String enrollIn2021PCAPIPlansUsingBEANEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_DefaultDestinationSiteID").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANEndPoint, getSessionMedicarePlansDrxDemoToSPA2021SpecId()));
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlans").replace("{SessionID}", "%s");
        Response getPlans = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getPlanID = getPlans.jsonPath().getString("MedicarePlans[1].ID");

        String enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Enroll").replace("{SessionID}", "%s")
                .replace("{planId}", getPlanID);
        String getPlansEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response getPlansEnroll = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint, sessionId), fileReader.getTestJsonFile(getPlansEnrollData));
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testEnrollInMedGapUsingBEAN_DefaultDestinationSiteID() {
        String enrollInMedGapUsingBEANEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_DefaultDestinationSiteID").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollInMedGapUsingBEANEndPoint, getSessionMedicarePlansDrxDemoToSPA2021SpecId()));
        ExtentLogger.pass("Test get plans request response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlans").replace("{SessionID}", "%s");
        Response getPlans = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANEndGetPlansPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollInMedGapUsingBEANQuestionsEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Questions").replace("{SessionID}", "%s");
        Response getQuestions = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollInMedGapUsingBEANQuestionsEndPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_QuestionsData"));
        ExtentLogger.pass("Test get Questions request response:- " + getQuestions.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollInMedGapUsingBEANSubmitAnswersEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/medsupSubmitAnswersCA.json";
        Response submitAnswers = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(enrollInMedGapUsingBEANSubmitAnswersEndPoint, sessionId), fileReader.getTestJsonFile(getSubmitAnswersData));
        ExtentLogger.pass("Test submit answers response:- " + submitAnswers.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);

        String enrollIn2021PCAPIPlansUsingBEANGetPlansEndPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlans").replace("{SessionID}", "%s");
        Response submitAnswersResponse = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(enrollIn2021PCAPIPlansUsingBEANGetPlansEndPoint, sessionId) + dataProp.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_GetPlansData"));
        Assert.assertEquals(response.getStatusCode(), 200);
        String getPlanID = getPlans.jsonPath().getString("MedicarePlans[1].ID");

        String enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint = prop.getProperty("EnrollIn2020PCAPIPlansUsingBEAN_Enroll").replace("{SessionID}", "%s")
                .replace("{planId}", getPlanID);
        String getPlansEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response getPlansEnroll = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(enrollIn2021PCAPIPlansUsingBEANEndPlansEnrollPoint, sessionId), fileReader.getTestJsonFile(getPlansEnrollData));
        ExtentLogger.pass("Test plan enroll response:- " + getPlansEnroll.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    private String getSessionMedicarePlansDrxDemoToSPA2021SpecId() {
        String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA.json";
        sessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return sessionId;
    }
}
