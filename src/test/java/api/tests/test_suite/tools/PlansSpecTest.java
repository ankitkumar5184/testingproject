package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PlansSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    String planId;

    @Test(priority = 0)
    public void testThreePlanSearch() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        String threePlanSearchEndpoint = prop.getProperty("Plans_Groovy_Three_Plans_Search");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), threePlanSearchEndpoint + dataProp.getProperty("THREE_PLANS_SEARCH"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }

    @Test(priority = 1)
    public void testPlanSearch() {
        String planSearchEndpoint = prop.getProperty("Plans_Search");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planSearchEndpoint + dataProp.getProperty("PLAN_SEARCH"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
        planId = response.jsonPath().getString("MedicarePlans[0].ID");
    }

    @Test(priority = 2)
    public void testPlanDetailRequest(){
        String planDetailRequestEndpoint = prop.getProperty("Plan_Detail").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planDetailRequestEndpoint);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("ID"), planId);
        assertNotNull(response.jsonPath().getString("PlanID"));
    }

    @Test(priority = 3)
    public void testPlanPharmacyRequestWithManyResponses(){
        String pharmacyWithManyParamEndpoint = prop.getProperty("Plan_Pharmacy_With_Many_Param").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyWithManyParamEndpoint + dataProp.getProperty("PHARMACY_WITH_MANY_PARAMS"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("PharmacyList[0].PharmacyID"));
        assertNotNull(response.jsonPath().getString("PharmacyList[0].Name"));
    }

    @Test(priority = 4)
    public void testPlanPharmacyRequestWithLargeRadius(){
        String pharmacyWithLargeRadiusEndpoint = prop.getProperty("Plan_Pharmacy_With_Many_Param").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyWithLargeRadiusEndpoint + dataProp.getProperty("PHARMACY_WITH_LARGE_RADIUS"));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("PharmacyList[0].PharmacyID"));
        assertNotNull(response.jsonPath().getString("PharmacyList[0].Name"));
    }

    @Test(priority = 5)
    public void testConvertIdsForPlans() {
        String convertPlanIDsEndpoint = prop.getProperty("Plan_ConvertId");
        String convertPlanIDsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/convertIds.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", convertPlanIDsEndpoint,
                fileReader.getTestJsonFile(convertPlanIDsData));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ContractID"));
    }
}