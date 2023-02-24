package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.UserNamePasswords;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class CrmMemberFlowSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String createSessionData;
    private String memberSearchSessionId;
    private String getSessionCrmMemberFlowSpecEndPoint;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String blankSessionIdResponse;
    private String firstName;
    private String lastName;
    private String ssoValue;
    private String phoneNumber;
    private String getZip;
    private String getFip;
    private String getEmailId;

    @Test(priority = 1)
    public void testCreateNewMemberFromCrmWithNoSSOValue(){
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, getSessionCrmMemberWithoutSsoValueSessionId()));
        firstName = response.jsonPath().get("Profile.FirstName");
        lastName = response.jsonPath().get("Profile.LastName");
        getZip = response.jsonPath().get("Zip");
        getFip = response.jsonPath().get("Fips");
        getEmailId = response.jsonPath().get("Profile.EmailAddress");
        assertEquals(firstName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(lastName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(getZip, dataProp.getProperty("CRM_MEMBER_ZIP"));
        assertEquals(getFip,dataProp.getProperty("CRM_MEMBER_FIPS"));
        assertEquals(getEmailId,dataProp.getProperty("EMAILID"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response Without SSO Value:- " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testCreateNewMemberFromCrmWithInvalidSessionAndNoSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse,GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode,GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response Without SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testCreateNewMemberFromCrmWithBlankSessionAndNoSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")));
        blankSessionIdResponse = response.jsonPath().get("Message");
        assertEquals(blankSessionIdResponse, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response Without SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testCreateNewMemberFromCrmWithSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, getSessionCrmMemberWithSsoValueSessionId()));
        firstName = response.jsonPath().get("Profile.FirstName");
        lastName = response.jsonPath().get("Profile.LastName");
        getZip = response.jsonPath().get("Zip");
        getFip = response.jsonPath().get("Fips");
        getEmailId = response.jsonPath().get("Profile.EmailAddress");
        ssoValue = response.jsonPath().get("SSOValue");
        phoneNumber = response.jsonPath().get("Profile.HomePhone");
        assertEquals(firstName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(lastName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(getZip, dataProp.getProperty("CRM_MEMBER_ZIP"));
        assertEquals(getFip,dataProp.getProperty("CRM_MEMBER_FIPS"));
        assertEquals(getEmailId,dataProp.getProperty("EMAILID"));
        assertEquals(ssoValue,dataProp.getProperty("SSO_VALUE"));
        assertEquals(phoneNumber,dataProp.getProperty("HOME_PHONE"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With SSO Value:- " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testCreateNewMemberFromCrmWithInvalidSessionAndSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse,GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode,GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testCreateNewMemberFromCrmWithBlankSessionAndSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")));
        blankSessionIdResponse = response.jsonPath().get("Message");
        assertEquals(blankSessionIdResponse, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    @Test(priority = 7)
    public void testCreateNewMemberFromCrmWithUpdatedSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, getUpdatedSsoValueCrmMemberWithSsoValueSessionId()));
        firstName = response.jsonPath().get("Profile.FirstName");
        lastName = response.jsonPath().get("Profile.LastName");
        getZip = response.jsonPath().get("Zip");
        getFip = response.jsonPath().get("Fips");
        getEmailId = response.jsonPath().get("Profile.EmailAddress");
        ssoValue = response.jsonPath().get("SSOValue");
        phoneNumber = response.jsonPath().get("Profile.HomePhone");
        assertEquals(firstName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(lastName, UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(getZip, dataProp.getProperty("CRM_MEMBER_ZIP"));
        assertEquals(getFip,dataProp.getProperty("CRM_MEMBER_FIPS"));
        assertEquals(getEmailId,dataProp.getProperty("EMAILID"));
        assertEquals(ssoValue,dataProp.getProperty("NEW_SSO_VALUE"));
        assertEquals(phoneNumber,dataProp.getProperty("NEW_HOME_PHONE"));
        assertEquals(response.getStatusCode(), 200);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With Updated SSO Value:- " + response.asPrettyString());
    }

    @Test(priority = 8)
    public void testCreateNewMemberFromCrmWithInvalidSessionAndUpdatedSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value));
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse,GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode,GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    @Test(priority = 9)
    public void testCreateNewMemberFromCrmWithBlankSessionAndUpdatedSSOValue(){
        getSessionCrmMemberFlowSpecEndPoint = prop.getProperty("CrmMemberSearch_GetSessionId").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionCrmMemberFlowSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")));
        blankSessionIdResponse = response.jsonPath().get("Message");
        assertEquals(blankSessionIdResponse, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
        assertEquals(response.getStatusCode(), 405);
        ExtentLogger.pass("Get Session Crm Member Flow Spec Response With SSO Value & Invalid Session ID :- " + response.asPrettyString());
    }

    private String getSessionCrmMemberWithoutSsoValueSessionId(){
        createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionCrmMemberFlowSpec.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionCrmMemberWithSsoValueSessionId(){
         createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSessionCrmMemberFlowSpec.json";
         memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
         return memberSearchSessionId;
}

    private String getUpdatedSsoValueCrmMemberWithSsoValueSessionId(){
         createSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/updateSsoValueCrmMemberFlowSpec.json";
         memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
         return memberSearchSessionId;
}
}