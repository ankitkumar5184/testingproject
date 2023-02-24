package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
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

public class PlansSpecPharmacyDetailsTest {

    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonMethods commonMethods = new CommonMethods();
    FileReader fileReader = new FileReader();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    String createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionPharmacyDetails.json";
    String createSessionPharmacyDetailsNoPharmacyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionPharmacyDetailsNoPharmacy.json";

    protected String plansSpecPharmacyDetailsSessionId;
    protected String plansSpecPharmacyDetailsEndpoint;

    @Test(priority = 1)
    public void testVerifyCreateSessionWithPharmacyData() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, getPlansSpecPharmacyDetailsSessionId(createSessionData)));
        ExtentLogger.pass("Test Create session with pharmacy data response:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("Pharmacies[0].PharmacyID"), dataProp.getProperty("PharmaciesFirst_PharmacyID"));
        Assert.assertTrue(response.jsonPath().getInt("Pharmacies[0].PharmacyRecordID") > 0);
        Assert.assertNotNull(response.jsonPath().getString("Pharmacies[0].Name"));
        Assert.assertNotNull(response.jsonPath().getString("Pharmacies[0].City"));
    }

    @Test(priority = 2)
    public void testVerifyCreateSessionWithPharmacyDataWithInvalidSessionId() {
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("INVALID_TEXT")));
        ExtentLogger.pass("Test Create session with pharmacy data response when invalid sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyCreateSessionWithPharmacyDataWithBlankSessionId() {
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test Create session with pharmacy data response when empty sessionId:- " + response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyCreateSessionWithNoPharmacyData() {
        // Get session Id
        Response responseWithNoPharmacyData = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, getPlansSpecPharmacyDetailsSessionId(createSessionPharmacyDetailsNoPharmacyData)));
        ExtentLogger.pass("Test Create session with no pharmacy data response:- " + responseWithNoPharmacyData.asPrettyString());
        Assert.assertEquals(responseWithNoPharmacyData.getStatusCode(), 200);
        Assert.assertNull(responseWithNoPharmacyData.jsonPath().getString("[0].Pharmacies"));

        //Get created session
        Response responseGetSession = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + plansSpecPharmacyDetailsSessionId);
        ExtentLogger.pass("Test Get Create session response:- " + responseGetSession.asPrettyString());
        Assert.assertEquals(responseGetSession.getStatusCode(), 200);
        Assert.assertNull(responseGetSession.jsonPath().getString("[0].Pharmacies"));
    }

    private String getPlansSpecPharmacyDetailsSessionId(String fileName) {
        plansSpecPharmacyDetailsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(fileName));
        ExtentLogger.pass("PlansSpec pharmacy details sessionId:- " + plansSpecPharmacyDetailsSessionId);
        return plansSpecPharmacyDetailsSessionId;
    }
}