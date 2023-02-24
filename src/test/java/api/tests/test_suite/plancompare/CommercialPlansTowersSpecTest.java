package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.UserNamePasswords;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CommercialPlansTowersSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String commercialPlansTowerSessionId;
    private String ID;

    @Test(priority = 1)
    public void testVerifyGetSession() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("CommercialPlansTower_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getCommercialPlansTowerSessionId()));
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("Zip"), ZipCodeValues.Zip_85004.value);
        assertEquals(response.jsonPath().get("Fips"), FipsValues.Fips_04013.value);
        assertEquals(response.jsonPath().get("Profile.FirstName"), UserNamePasswords.FIRSTNAME_TEXT.value);
        assertEquals(response.jsonPath().get("Profile.LastName"), UserNamePasswords.LASTNAME_TEXT.value);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("CommercialPlansTower_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        ExtentLogger.pass("Test CommercialPlansTower get session with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("CommercialPlansTower_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        ExtentLogger.pass("Test CommercialPlansTower get session with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlansAndCheckPlanDrugCoverage() {
        String getPlansAndCheckPlanDrugCoverageEndpoint = prop.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverage").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansAndCheckPlanDrugCoverageEndpoint, commercialPlansTowerSessionId) + dataProp.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverageData"));
        ExtentLogger.pass("Test CommercialPlansTower get plans and check plan drug coverage response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        ID = response.jsonPath().get("MedicarePlans[0].ID");
        assertTrue(ID.length() > 2);
        String carrierName = response.jsonPath().get("MedicarePlans[0].CarrierName");
        Assert.assertTrue(carrierName.length() > 2);
    }

    @Test(priority = 5)
    public void testVerifyGetPlansAndCheckPlanDrugCoverageWithInvalidSessionId() {
        String getPlansAndCheckPlanDrugCoverageEndpoint = prop.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverage").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansAndCheckPlanDrugCoverageEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverageData"));
        ExtentLogger.pass("Test CommercialPlansTower get plans and check plan drug coverage with invalid SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansAndCheckPlanDrugCoverageWithBlankSessionId() {
        String getPlansAndCheckPlanDrugCoverageEndpoint = prop.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverage").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansAndCheckPlanDrugCoverageEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("CommercialPlansTower_GetPlansAndCheckPlanDrugCoverageData"));
        ExtentLogger.pass("Test CommercialPlansTower get plans and check plan drug coverage with blank SessionID response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyPlanDetailRequestAndCheckPlanDrugCoverage() {
        String getPlansAndCheckPlanDrugCoverageEndpoint = prop.getProperty("CommercialPlansTower_PlanDetailRequestAndCheckPlanDrugCoverage").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlansAndCheckPlanDrugCoverageEndpoint + dataProp.getProperty("CommercialPlansTower_PlanDetailRequestAndCheckPlanDrugCoverageData"));
        ExtentLogger.pass("Test CommercialPlansTower plan detail request and check PlanDrugCoverage response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("ID"), ID);
        assertEquals(response.jsonPath().get("PlanDrugCoverage[0].NDC"), dataProp.getProperty("COMMERCIALPLANSTOWER_NDC"));
        assertEquals(response.jsonPath().get("PlanDrugCoverage[0].LabelName"), dataProp.getProperty("COMMERCIALPLANSTOWER_LABELNAME"));
        int tierNumber = response.jsonPath().get("PlanDrugCoverage[0].TierNumber");
        assertTrue(tierNumber >= 0);
    }

    private String getCommercialPlansTowerSessionId() {
        String createCommercialPlansTowerSessionIdData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionTowersData.json";
        commercialPlansTowerSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createCommercialPlansTowerSessionIdData));
        return commercialPlansTowerSessionId;
    }
}