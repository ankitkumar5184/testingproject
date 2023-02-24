package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import api.base.helpers.CommonDateTimeMethods;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class AgentsTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    PutRequest putRequest = new PutRequest();
    CommonMethods commonMethods = new CommonMethods();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();

    @Test(priority = 1)
    public void testVerifyGetCarriers() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        String getCarriersEndpoint = prop.getProperty("Agents_GetCarriers");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getCarriersEndpoint);
        ExtentLogger.pass("Test AgentsTest get carriers case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int id = Integer.parseInt(dataProp.getProperty("GetCarriers_ID"));
        assertEquals(response.jsonPath().getInt("[0].ID"), id);
        assertEquals(response.jsonPath().get("[0].Name"), dataProp.getProperty("GetCarriers_Name"));
        assertEquals(response.jsonPath().get("[0].LongName"), dataProp.getProperty("GetCarriers_Name"));
    }

    @Test(priority = 2)
    public void testVerifySearchAgents() {
        String searchAgentsEndpoint = prop.getProperty("Agents_GetSearchAgents");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), searchAgentsEndpoint + dataProp.getProperty("Agents_GetSearchAgentsData"));
        ExtentLogger.pass("Test AgentsTest search agents case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int totalCount = Integer.parseInt(dataProp.getProperty("TotalCount"));
        assertEquals(response.jsonPath().getInt("TotalCount"), totalCount);
    }

    @Test(priority = 3)
    public void testVerifyGetAgent() {
        String getAgentEndpoint = prop.getProperty("Agents_GetAgent").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getAgentEndpoint);
        ExtentLogger.pass("Test AgentsTest get agent case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("ExternalUserId"), dataProp.getProperty("AgentId"));
    }

    @Test(priority = 4)
    public void testVerifyCreateAgent() {
        String unique = commonDateTimeMethods.getUniqueNumber();
        String createAgentEndpoint = prop.getProperty("Agents_PostCreateAgent");
        String createAgentData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/createAgentData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", createAgentEndpoint, fileReader.getTestJsonFile(createAgentData).replace("{uniqueNumber}", unique));
        ExtentLogger.pass("Test AgentsTest create agent case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 201);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("UserName"), dataProp.getProperty("CreateAgent_UserName").replace("{uniqueNumber}", unique));
        assertEquals(response.jsonPath().get("FirstName"), dataProp.getProperty("FirstName"));
        assertEquals(response.jsonPath().get("LastName"), dataProp.getProperty("LastName"));
    }

    @Test(priority = 5)
    public void testVerifyCreateAgentWithBlankBodyData() {
        String createAgentEndpoint = prop.getProperty("Agents_PostCreateAgent");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", createAgentEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test AgentsTest create agent with blank body data case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentID\"][0]"), ToolsApiErrorMessages.AGENT_ID_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.UserName\"][0]"), ToolsApiErrorMessages.USERNAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.FirstName\"][0]"), ToolsApiErrorMessages.FIRST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.LastName\"][0]"), ToolsApiErrorMessages.LAST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentRole\"][0]"), ToolsApiErrorMessages.AGENT_ROLE_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.IsActive\"][0]"), ToolsApiErrorMessages.IS_ACTIVE_MISSING.value);
    }

    @Test(priority = 6)
    public void testVerifyCreateAgentCopy() {
        String createAgentCopyEndpoint = prop.getProperty("Agents_PutCreateAgentCopy").replace("{AgentID}", dataProp.getProperty("AgentId"));
        String createAgentCopyData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/createAgentCopyData.json";
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), createAgentCopyEndpoint, fileReader.getTestJsonFile(createAgentCopyData));
        ExtentLogger.pass("Test AgentsTest create agent copy case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("FirstName"), dataProp.getProperty("FirstName"));
        assertEquals(response.jsonPath().get("LastName"), dataProp.getProperty("LastName"));
    }

    @Test(priority = 7)
    public void testVerifyCreateAgentCopyWithBlankBodyData() {
        String createAgentCopyEndpoint = prop.getProperty("Agents_PutCreateAgentCopy").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), createAgentCopyEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test AgentsTest create agent copy with blank body data case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentID\"][0]"), ToolsApiErrorMessages.AGENT_ID_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.FirstName\"][0]"), ToolsApiErrorMessages.FIRST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.LastName\"][0]"), ToolsApiErrorMessages.LAST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentRole\"][0]"), ToolsApiErrorMessages.AGENT_ROLE_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.IsActive\"][0]"), ToolsApiErrorMessages.IS_ACTIVE_MISSING.value);
    }

    @Test(priority = 8)
    public void testVerifyGetSellingPermissions() {
        String getSellingPermissionsEndpoint = prop.getProperty("Agents_GetSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getSellingPermissionsEndpoint);
        ExtentLogger.pass("Test AgentsTest get selling permissions case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int carrierId = Integer.parseInt(dataProp.getProperty("GetSellingPermissions_CarrierId"));
        assertEquals(response.jsonPath().getInt("[0].CarrierID"), carrierId);
        assertEquals(response.jsonPath().get("[0].AgentProducerID"), dataProp.getProperty("GetSellingPermissions_AgentProducerID"));
    }

    @Test(priority = 9)
    public void testVerifyPostSellingPermissions() {
        String postSellingPermissionsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/postSellingPermissionsData.json";
        String postSellingPermissionsEndpoint = prop.getProperty("Agents_PostSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", postSellingPermissionsEndpoint, fileReader.getTestJsonFile(postSellingPermissionsData));
        ExtentLogger.pass("Test AgentsTest post selling permissions case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 10)
    public void testVerifyPostSellingPermissionsWithBlankBodyData() {
        String postSellingPermissionsEndpoint = prop.getProperty("Agents_PostSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", postSellingPermissionsEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        ExtentLogger.pass("Test AgentsTest post selling permissions with blank body data case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.sellingPermissions[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }
}