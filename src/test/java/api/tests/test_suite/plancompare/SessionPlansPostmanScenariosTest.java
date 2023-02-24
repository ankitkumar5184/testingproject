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
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class SessionPlansPostmanScenariosTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    PostmanSessionsScenariosTest postmanSessionsScenariosTest = new PostmanSessionsScenariosTest();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();

    private String planSessionId;
    private String getPlanType;
    private String medicarePlansID;
    private String getEnrollActionType;
    private String getMethod;
    private String getPlanSubType;
    private String getUrl;
    private String getPlanYear;
    private String getMedicarePlansId;
    private String getID;

    @Test
    public void testVerifyGetPlansSCS_43234(){
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, getSessionPlansSessionId()) +
                dataProp.getProperty("Plans_GetPlans_SCS_43234"));
        medicarePlansID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plans SCS-43234 response:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testVerifyGetPlansWTWIssue(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansWTWIssue"));
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plans WTW Issue response:- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testVerifyGetMedigapPlans(){
        postmanSessionsScenariosTest.testCreateSessionWithMedGapQuestions();
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, postmanSessionsScenariosTest.getMedGapSessionId) +
                dataProp.getProperty("Plans_GetMedigapPlans"));
        getID = response.jsonPath().getString("MedigapPlans[0].ID");
        assertFalse(getID.isEmpty());
        getPlanType = response.jsonPath().getString("MedigapPlans[0].PlanType");
        assertEquals(getPlanType, dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Medigap Plans response:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testVerifyGetDentalPlans(){
        postmanSessionsScenariosTest.testCreateSessionWithDentalVisionQuestions();
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, postmanSessionsScenariosTest.getDentalVisionSessionId) +
                dataProp.getProperty("Plans_GetDentalPlans"));
        getPlanType = response.jsonPath().getString("AncillaryPlans[0].PlanType");
        getPlanSubType = response.jsonPath().getString("AncillaryPlans[0].PlanSubType");
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertFalse(getID.isEmpty());
        assertEquals(getPlanType, dataProp.getProperty("DENTAL_PLAN_TYPE"));
        assertTrue(getPlanSubType.contains(dataProp.getProperty("DENTAL_PLAN_SUB_TYPE")));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Dental Plans response:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testVerifyGetVisionPlans(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, postmanSessionsScenariosTest.getDentalVisionSessionId)+
                dataProp.getProperty("Plans_GetVisionPlans"));
        getPlanType = response.jsonPath().getString("AncillaryPlans[0].PlanType");
        getPlanSubType = response.jsonPath().getString("AncillaryPlans[0].PlanSubType");
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertFalse(getID.isEmpty());
        assertEquals(getPlanType, dataProp.getProperty("VISIONS_PLAN_TYPE"));
        assertTrue(getPlanSubType.contains(dataProp.getProperty("VISIONS_PLAN_SUB_TYPE")));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Vision Plans response:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testVerifyGetHIPPlans(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, createHipSession())+
                dataProp.getProperty("Plans_GetHIPPlans"));
        String getHipPlanType = response.jsonPath().getString("AncillaryPlans[0].PlanType");
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertFalse(getID.isEmpty());
        assertTrue(getHipPlanType.contains(dataProp.getProperty("HIP_PLAN_TYPE")));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get HIP Plans response:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetails(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId)+
                String.format(dataProp.getProperty("Plans_GetPlanDetailsEndPoint").replace("{PlansID}", "%s"), medicarePlansID));
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details response:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsMedSup(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId)+
                String.format(dataProp.getProperty("Plans_GetPlanDetailsMedSup").replace("{PlansID}", "%s"), medicarePlansID));
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details MedSup response:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsDental(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsDental").replace("{PlansID}", "%s"), medicarePlansID));
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details Dental response:- " + response.asPrettyString());
    }

    @Test(priority = 10)
    public void testVerifyGetPlanDetailsVision(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsVision").replace("{PlansID}", "%s"), medicarePlansID));
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details Vision response:- " + response.asPrettyString());
    }

    @Test(priority = 11)
    public void testVerifyGetPlanDetailsHardcoded(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsHardcoded").replace("{PlansID}", "%s"), medicarePlansID));
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plan Details Hardcoded response:- " + response.asPrettyString());
    }

    @Test(priority = 12)
    public void testVerifyGetPlansPlanSmart(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansPlanSmartPostman"));
        getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plans PlanSmart response:- " + response.asPrettyString());
    }

    @Test(priority = 13)
    public void testVerifyGetPlansManyParams(){
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansManyParams"));
        getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Plans Many Params response:- " + response.asPrettyString());
    }

    @Test(priority = 14)
    public void testVerifyPostEnroll(){
        String postPlansEnrollmentBodyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/enrollData.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON" ,prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId, medicarePlansID),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        getEnrollActionType = response.jsonPath().getString("EnrollActionType");
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getEnrollActionType, dataProp.getProperty("ENROLL_ACTION_TYPE"));
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Enroll response:- " + response.asPrettyString());
    }

    @Test(priority = 15)
    public void testVerifyPostPlanEnrollJson(){
        String postPlansEnrollmentBodyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollJson.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON" ,prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId, medicarePlansID),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        getEnrollActionType = response.jsonPath().getString("EnrollActionType");
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getEnrollActionType, dataProp.getProperty("ENROLL_ACTION_TYPE"));
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Plan Enroll Json response:- " + response.asPrettyString());
    }
    
    @Test(priority = 16)
    public void testVerifyPostPlanEnrollJsonMinimum(){
        String postPlansEnrollmentBodyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollmentJsonMinimum.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON" ,prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId) +
                        dataProp.getProperty("PlanEnrollJSONMinimum").replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                        fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        getEnrollActionType = response.jsonPath().getString("EnrollActionType");
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getEnrollActionType, dataProp.getProperty("ENROLL_ACTION_TYPE"));
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Plan Enroll Json With Minimum data response:- " + response.asPrettyString());
    }

    @Test(priority = 16)
    public void testVerifyPlanEnrollWithRiders(){
        String postPlansEnrollmentBodyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/planEnrollWithRiders.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnrollment").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId) + dataProp.getProperty("PlanEnrollWithRiders").
                        replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                        fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        getEnrollActionType = response.jsonPath().getString("EnrollActionType");
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getEnrollActionType, dataProp.getProperty("ENROLL_ACTION_TYPE"));
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Plan Enroll Json With Riders data response:- " + response.asPrettyString());
    }

    private String createHipSession(){
        String createSessionHip = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionHipData.json";
        String getHipPlansSessionID = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionHip));
        return getHipPlansSessionID;
    }

    private String getSessionPlansSessionId(){
        String postCreateSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionApiData.json";
        planSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return planSessionId;
    }
}