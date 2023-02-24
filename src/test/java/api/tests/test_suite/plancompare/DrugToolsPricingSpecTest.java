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
import static org.testng.Assert.assertEquals;

public class DrugToolsPricingSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String createSessionData;
    private String memberSearchSessionId;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String getSession_Phar2418209EndPoint;
    private String add_ToolsPhar2418209;
    private String getSession_Phar0581808EndPoint;
    private String add_ToolsPricingForMissingPharmacyId;

    @Test(priority = 1)
    public void testFullCostForCertainDrugAndPlan() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String add_tools7679Data = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_7679.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("DrugPricingTool_PlanDetail").replace("{PlanId_Tools_7679}", dataProp.getProperty("PlanId_Tools_7679")),
                fileReader.getTestJsonFile(add_tools7679Data));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PlanId_Tools_7679"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testDrugThatDrugFootnotesArePresent() {
        String getSessionDrugFootnotesEndPoint = prop.getProperty("DrugFootNotes_EndPoint").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("DrugFootNotes_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionDrugFootnotesEndPoint, getSessionDrugFootnotesSessionId()) + dataProp.getProperty("DrugFootNotes_EndPointData"));
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Get Session For Drug That Drug Footnotes Are Present:- " + response.asPrettyString());
    }

    //  Test PC Pricing for SME-14805
    @Test(priority = 3)
    public void testPcPricing_1_forSme_14805() {
        getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, getSessionIdToolsPricing_PC_Phar0581808()) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Get Session For Pc Pricing 1 For Sme 14805:- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testPcPricing_2_forSme_14805() {
        getSession_Phar2418209EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar2418209EndPoint, getSessionIdToolsPricing_PC_Phar2418209()) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Session For Pc Pricing 2 For Sme 14805:- " + response.asPrettyString());
    }

    //  Test Tools Pricing for SME-14805
    @Test(priority = 5)
    public void testToolsPricing_1_forSme_14805() {
        add_ToolsPhar2418209 = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_pharm2418209.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                fileReader.getTestJsonFile(add_ToolsPhar2418209));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session to Tools Pricing 1 For SME 14805:- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testToolsPricing_2_forSme_14805() {
        add_ToolsPhar2418209 = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_pharm2418209.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2")),
                fileReader.getTestJsonFile(add_ToolsPhar2418209));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session to Tools Pricing 2 For SME 14805:- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testToolsPricing_3_forSme_14805() {
        String add_ToolsPhar0581808 = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_pharm0581808.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                fileReader.getTestJsonFile(add_ToolsPhar0581808));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session to Tools Pricing 3 For SME 14805:- " + response.asPrettyString());
    }

    //  Test Tools Pricing For Missing Pharmacy ID Issue
    @Test(priority = 8)
    public void testToolsPricingForMissingPharmacyId_1_Issue() {
        add_ToolsPricingForMissingPharmacyId = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_pharm2418209.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                fileReader.getTestJsonFile(add_ToolsPricingForMissingPharmacyId));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session To Tools Pricing For Missing Pharmacy Id 1 Issue:- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testToolsPricingForMissingPharmacyId_2_Issue() {
        add_ToolsPricingForMissingPharmacyId = System.getProperty("user.dir") + "/src/test/resources/api.testdata/tools_pharm0581808_NewZip.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                fileReader.getTestJsonFile(add_ToolsPricingForMissingPharmacyId));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session To Tools Pricing For Missing Pharmacy Id 2 Issue:- " + response.asPrettyString());
    }

    //  Test PC Pricing Against New Tools EndPoint
    @Test(priority = 10)
    public void testTestPcPricingAgainstNewToolsEndPoint() {
        String getSession_PcPricing_Ca_10_EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcPricing_Ca_10_EndPoint, getSessionIdCa_10_PcPricingAgainstNewTools()) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Get Session For Test Pc Pricing Against New Tools:- " + response.asPrettyString());
    }

    //  Test Tools Pricing Against New Tools EndPoint
    @Test(priority = 11)
    public void testToolsPricingAgainstNewToolsEndPoint() {
        String add_ToolsPricing = System.getProperty("user.dir") + "/src/test/resources/api.testdata/toolsCA_10drugs.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                fileReader.getTestJsonFile(add_ToolsPricing));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session To Tools Pricing Against New Tools:- " + response.asPrettyString());
    }

    //  Test Pc Vs Tools For Case With Drug Footnotes
    @Test(priority = 12)
    public void testPcVsToolsForCaseWithDrugFootnotes() {
        String getSession_PcVsToolsForFootnotesEndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_DRUG_FOOT_NOTE_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcVsToolsForFootnotesEndPoint, getSessionId_Test_Ca_Oxy()) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Get Session Pc Vs Tools For Case With Drug Footnotes:- " + response.asPrettyString());
    }

    //  Test Tools Vs Pc For Case With Drug Footnotes
    @Test(priority = 13)
    public void testToolsVsPcForCaseWithDrugFootnotes() {
        String add_ToolsPricing = System.getProperty("user.dir") + "/src/test/resources/api.testdata/toolsCA_Oxy.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                fileReader.getTestJsonFile(add_ToolsPricing));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session To Tools Vs Pc For Case With Drug Footnotes:- " + response.asPrettyString());
    }

    //  Test Small PC Pricing Difference With Mail Order
    @Test(priority = 14)
    public void testSmall_Pc_PricingDifferenceWithMailOrder() {
        String getSession_PcSmallPricingDifferenceEndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcSmallPricingDifferenceEndPoint, getSessionId_Test_Ca_Pricing()) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Test Small PC Pricing Difference With Mail Order:- " + response.asPrettyString());
    }

    //  Test Small Tools Pricing Difference With Mail Order
    @Test(priority = 15)
    public void testSmall_Tools_PricingDifferenceWithMailOrder() {
        String add_ToolsSmallPricing = System.getProperty("user.dir") + "/src/test/resources/api.testdata/toolsCA_Pricing.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                fileReader.getTestJsonFile(add_ToolsSmallPricing));
        String idValue = response.jsonPath().getString("ID");
        assertEquals(idValue, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Add Session To Small Tools Pricing Difference With Mail Order:- " + response.asPrettyString());
    }

    @Test(priority = 16)
    public void testPcPricingForSme_14805_WithInvalidSessions() {
        String getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Pc Pricing For Sme-14805 With Invalid Sessions:- " + response.asPrettyString());
    }

    @Test(priority = 17)
    public void testPcPricingForSme_14805_WithBlankSessions() {
        String getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        assertEquals(response.getStatusCode(), 404);
        ExtentLogger.pass("Get Session Pc Pricing For Sme-14805 With Blank Sessions:- " + response.asPrettyString());
    }

    @Test(priority = 18)
    public void testToolsPostAPIsWithBlankBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                CommonValues.BLANK_BODY_VALID_REQUEST.value);
        assertEquals(response.getStatusCode(), 400);
        ExtentLogger.pass("Add Session To Tools Pricing Against New Tools:- " + response.asPrettyString());
    }

    private String getSessionDrugFootnotesSessionId() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createsession_footnoes.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdToolsPricing_PC_Phar2418209() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createsession_pharm2418209.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdToolsPricing_PC_Phar0581808() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createsession_pharm0581808.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdCa_10_PcPricingAgainstNewTools() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA_10drugs.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionId_Test_Ca_Oxy() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA_Oxy.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionId_Test_Ca_Pricing() {
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCA_Pricing.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }
}