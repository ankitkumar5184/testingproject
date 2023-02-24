package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PlansTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    @Test(priority = 0)
    public void testSessionLessPlansDetails() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        String sessionLessPlansDetailsEndpoint = prop.getProperty("Plans_PostSessionLessPlanDetails");
        String sessionLessPlansDetailsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/sessionlessPlansDetails.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", sessionLessPlansDetailsEndpoint + dataProp.getProperty("Plan_ID"),
                fileReader.getTestJsonFile(sessionLessPlansDetailsData));
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("ID"), dataProp.getProperty("Plan_ID"));
        assertNotNull(response.jsonPath().getString("ContractID"));
        assertNotNull(response.jsonPath().getString("PlanID"));
    }

    @Test(priority = 1)
    public void testSessionLessPlansDetailsWithBlankBodyData() {
        String sessionLessPlansDetailsEndpoint = prop.getProperty("Plans_PostSessionLessPlanDetails");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", sessionLessPlansDetailsEndpoint + dataProp.getProperty("Plan_ID"),
                CommonValues.BLANK_BODY_VALID_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.planRequest[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 2)
    public void testConvertPlanIDs() {
        String convertPlanIDsEndpoint = prop.getProperty("Plans_PostConvertPlanIDs");
        String convertPlanIDsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/convertPlanIds.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", convertPlanIDsEndpoint,
                fileReader.getTestJsonFile(convertPlanIDsData));
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("MedicarePlans[0].ContractID"), dataProp.getProperty("CONTRACT_ID"));
        assertEquals(response.jsonPath().getString("MedicarePlans[0].SegmentID"), dataProp.getProperty("SEGMENT_ID"));
        assertEquals(response.jsonPath().getString("MedicarePlans[0].PlanYear"), dataProp.getProperty("PLAN_YEAR"));
    }

    @Test(priority = 3)
    public void testConvertPlanIDsWithBlankBodyData() {
        String convertPlanIDsEndpoint = prop.getProperty("Plans_PostConvertPlanIDs");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", convertPlanIDsEndpoint,
                CommonValues.BLANK_BODY_VALID_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.plansToConvert[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 4)
    public void testGetPlanDetails() {
        String getPlanDetailsEndpoint = prop.getProperty("Plans_GetPlanDetails");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlanDetailsEndpoint + dataProp.getProperty("Plan_ID"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("ContractID"));
        assertNotNull(response.jsonPath().getString("PlanID"));
    }

    @Test(priority = 5)
    public void testGetPlanDetailsCollection() {
        String getPlanDetailsEndpoint = prop.getProperty("Plans_GetPlanDetailsCollection");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlanDetailsEndpoint + dataProp.getProperty("GET_PLANS_DETAILS_DATA"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }

    @Test(priority = 6)
    public void testPlanSearch() {
        String planSearchEndpoint = prop.getProperty("Plans_GetPlanSearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planSearchEndpoint + dataProp.getProperty("PLAN_SEARCH_DATA"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("AncillaryPlans[0].ID"));
        assertNotNull(response.jsonPath().getString("AncillaryPlans[0].PlanID"));
    }

    @Test(priority = 7)
    public void testGetAllPlansFootPrint() {
        String getAllPlansFootPrintEndpoint = prop.getProperty("Plans_GetAllPlansFootPrint");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getAllPlansFootPrintEndpoint + dataProp.getProperty("GET_ALL_PLAN_FOOTPRINT"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("[0].ID"));
        assertNotNull(response.jsonPath().getString("[0].CarrierName"));
    }

    @Test(priority = 8)
    public void testPlanSearchMedicare() {
        String planSearchMedicareEndpoint = prop.getProperty("Plans_PlanSearchMedicare");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planSearchMedicareEndpoint + dataProp.getProperty("PLAN_SEARCH_MEDICARE"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].PlanType"));
    }

    @Test(priority = 9)
    public void testGetPlanNetworkPharmacySearch() {
        String getPlanNetworkPharmacySearchEndpoint = prop.getProperty("Plans_GetPlanNetworkPharmacySearch").replace("{planId}",dataProp.getProperty("Plan_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlanNetworkPharmacySearchEndpoint + dataProp.getProperty("GET_PLAN_NETWORK_PHARMACY_SEARCH"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("TotalCount"));
        assertNotNull(response.jsonPath().getString("Radius"));
    }

    @Test(priority = 10)
    public void testGetPlansByIDs() {
        String getPlansByIDsEndpoint = prop.getProperty("Plans_GetPlansByIDs");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlansByIDsEndpoint + dataProp.getProperty("GET_PLANS_BY_IDs"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }

    @Test(priority = 11)
    public void testGetPlansByCPIDs() {
        String getPlansByCPIDsEndpoint = prop.getProperty("Plans_GetPlansByCPIDs");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPlansByCPIDsEndpoint + dataProp.getProperty("GET_PLANS_BY_CPIDs"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }
}