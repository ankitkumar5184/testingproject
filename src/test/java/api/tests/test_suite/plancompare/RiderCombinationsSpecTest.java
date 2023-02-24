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
import static org.testng.Assert.assertNotNull;

public class RiderCombinationsSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    String riderCombinationsSpecTestSessionId;
    String getFirstPlanType, getSecondPlanType;
    String getFirstMedGapId, getSecondMedGapId;

    @Test(priority = 1)
    public void testGetPlans() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String getPlansEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, getRiderCombinationsSpecTestSessionId())
                + dataProp.getProperty("RiderCombinationsSpec_GetPlans_Data"));
        assertEquals(response.getStatusCode(), 200);
        String responseBody = response.getBody().asString();
        assertThat(responseBody, containsString("Humana"));
        getFirstMedGapId = response.jsonPath().getString("MedicarePlans[0].ID");
        getFirstPlanType = response.jsonPath().getString("MedicarePlans[0].PlanType");
        getSecondPlanType = response.jsonPath().getString("MedicarePlans[1].PlanType");
        assertNotNull(getFirstMedGapId);
        getSecondMedGapId = response.jsonPath().getString("MedicarePlans[2].ID");
        assertNotNull(getSecondMedGapId);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanType"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanSubType"));
    }

    @Test(priority = 2)
    public void testGetPlansWithInvalidSessionId() {
        String getPlansEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("RiderCombinationsSpec_GetPlans_Data"));
        ExtentLogger.pass("Test Get Plans With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testGetPlansWithBlankSessionId() {
        String getPlansEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("RiderCombinationsSpec_GetPlans_Data"));
        ExtentLogger.pass("Test Get Plans With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 4)
    public void testGetPlanDetailsForFirstTestPlan(){
        String getPlanDetailsForFirstTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForFirstTestPlanEndpoint, riderCombinationsSpecTestSessionId) + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For First Test Plan response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("ID"), getFirstMedGapId);
        assertEquals(response.jsonPath().getString("PlanType"), getFirstPlanType);
    }

    @Test(priority = 5)
    public void testGetPlanDetailsForFirstTestPlanWithInvalidSessionId() {
        String getPlanDetailsForFirstTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForFirstTestPlanEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For First Test Plan Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testGetPlanDetailsForFirstTestPlanWithBlankSessionId() {
        String getPlanDetailsForFirstTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForFirstTestPlanEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For First Test Plan With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 7)
    public void testGetPlanDetailsForSecondTestPlan(){
        String getPlanDetailsForSecondTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getSecondMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForSecondTestPlanEndpoint, riderCombinationsSpecTestSessionId) + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For Second Test Plan response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("ID"), getSecondMedGapId);
        assertEquals(response.jsonPath().getString("PlanType"), getSecondPlanType);
    }

    @Test(priority = 8)
    public void testGetPlanDetailsForSecondTestPlanWithInvalidSessionId() {
        String getPlanDetailsForSecondTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getSecondMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForSecondTestPlanEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For Second Test Plan Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testGetPlanDetailsForSecondTestPlanWithBlankSessionId() {
        String getPlanDetailsForSecondTestPlanEndpoint = prop.getProperty("RiderCombinationsSpec_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanId}", getSecondMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsForSecondTestPlanEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("RiderCombinationsSpec_GetPlan_Data"));
        ExtentLogger.pass("Test Get Plan Details For Second Test Plan With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testEnrollWithInvalidRiderCombinationsAndSeeError(){
        String getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint, riderCombinationsSpecTestSessionId), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Invalid Rider Combinations And See Error response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);

        String getEnrollWith1and2ValidRidersEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("MultipleRiderPlanId")).replace("{RiderID}", dataProp.getProperty("MultipleRiderValues"));
        Response enrollWith1and2ValidRiders = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWith1and2ValidRidersEndPoint, riderCombinationsSpecTestSessionId), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With 1 and 2 Valid Riders response:- " + enrollWith1and2ValidRiders.asPrettyString());
        assertEquals(enrollWith1and2ValidRiders.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testEnrollWithInvalidRiderCombinationsAndSeeErrorWithInvalidSessionId(){
        String getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Invalid Rider Combinations And See Error With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testEnrollWithInvalidRiderCombinationsAndSeeErrorWithBlankSessionId(){
        String getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Invalid Rider Combinations And See Error With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 13)
    public void testEnrollWithInvalidRiderCombinationsAndSeeErrorWithBlankBodyData(){
        String getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", getFirstMedGapId).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithInvalidRiderCombinationsAndSeeErrorEndPoint, riderCombinationsSpecTestSessionId), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        ExtentLogger.pass("Test Enroll With Invalid Rider Combinations And See Error With Blank Body Data response:- " + response.asPrettyString());
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 14)
    public void testEnrollWithValidRiderCombinationsPlanRiderCombination(){
        String getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("RiderPlanId")).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint, riderCombinationsSpecTestSessionId), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Valid Rider Combinations Plan Rider Combination response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 15)
    public void testEnrollWithValidRiderCombinationsPlanRiderCombinationWithInvalidSessionId(){
        String getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("RiderPlanId")).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Valid Rider Combinations Plan Rider Combination With Invalid SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 16)
    public void testEnrollWithValidRiderCombinationsPlanRiderCombinationWithBlankSessionId(){
        String getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("RiderPlanId")).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        String getEnrollData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollData));
        ExtentLogger.pass("Test Enroll With Valid Rider Combinations Plan Rider Combination With Blank SessionId response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 17)
    public void testEnrollWithValidRiderCombinationsPlanRiderCombinationWithBlankBodyData(){
        String getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint = prop.getProperty("RiderCombinationsSpec_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("RiderPlanId")).replace("{RiderID}", dataProp.getProperty("RiderValue"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollWithValidRiderCombinationsPlanRiderCombinationEndPoint, riderCombinationsSpecTestSessionId), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        ExtentLogger.pass("Test  Enroll With Valid Rider Combinations Plan Rider Combination With Blank Body Data response:- " + response.asPrettyString());
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getRiderCombinationsSpecTestSessionId() {
        riderCombinationsSpecTestSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return riderCombinationsSpecTestSessionId;
    }
}