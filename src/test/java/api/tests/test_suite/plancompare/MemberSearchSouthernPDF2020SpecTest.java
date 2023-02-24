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

public class MemberSearchSouthernPDF2020SpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String memberSearchSouthernPDF2020SessionId;

    @Test(priority = 1)
    public void testVerifySCS_29577() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        String sCS_29577Endpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577Endpoint, getMemberSearchSouthernPDF2020SessionId()) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_testVerifySCS_29577"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 2)
    public void testVerifySCS_29577WithInvalidSessionId() {
        String sCS_29577Endpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577Endpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_testVerifySCS_29577"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifySCS_29577WithBlankSessionId() {
        String sCS_29577Endpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577Endpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_testVerifySCS_29577"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 4)
    public void testVerifySCS_29577StageSouthern() {
        String sCS_29577StageSouthernEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577StageSouthernEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SCS_29577StageSouthern"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 stage southern case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testVerifySCS_29577StageSouthernWithInvalidSessionId() {
        String sCS_29577StageSouthernEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577StageSouthernEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SCS_29577StageSouthern"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 stage southern with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifySCS_29577StageSouthernWithBlankSessionId() {
        String sCS_29577StageSouthernEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(sCS_29577StageSouthernEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SCS_29577StageSouthern"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest SCS_29577 stage southern with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 7)
    public void testVerifySearchByMemberName() {
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchByMemberNameEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchByMemberNameData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search by member name case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 8)
    public void testVerifySearchByMemberNameWithInvalidSessionId() {
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchByMemberNameEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchByMemberNameData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search by member name with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifySearchByMemberNameWithBlankSessionId() {
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchByMemberNameEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchByMemberNameData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search by member name with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testVerifySearchForEnrollmentWithConfirmationNumber() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumber"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment with confirmation number case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testVerifySearchForEnrollmentWithConfirmationNumberWithInvalidSessionId() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumber"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment with confirmation number with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testVerifySearchForEnrollmentWithConfirmationNumberWithBlankSessionId() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumber"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment with confirmation number with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifySearchForEnrollmentJustPhone() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustPhoneEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just phone case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 14)
    public void testVerifySearchForEnrollmentJustPhoneWithInvalidSessionId() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just phone with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifySearchForEnrollmentJustPhoneWithBlankSessionId() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just phone with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 16)
    public void testVerifySearchForEnrollmentJustConf() {
        String searchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustConfEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just conf case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 17)
    public void testVerifySearchForEnrollmentJustConfWithInvalidSessionId() {
        String searchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustConfEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just conf with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 18)
    public void testVerifySearchForEnrollmentJustConfWithBlankSessionId() {
        String searchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustConfEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just conf with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 19)
    public void testVerifySearchForEnrollmentJustLast() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustLastEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentJustLast"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just last case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 20)
    public void testVerifySearchForEnrollmentJustLastWithInvalidSessionId() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustLastEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentJustLast"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just last with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 21)
    public void testVerifySearchForEnrollmentJustLastWithBalnkSessionId() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustLastEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MemberSearchSouthernPDF2020SpecTest_SearchForEnrollmentJustLast"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment just last with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 22)
    public void testVerifySearchForEnrollmentPartialPhone() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 23)
    public void testVerifySearchForEnrollmentPartialPhoneWithInvalidSessionId() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 24)
    public void testVerifySearchForEnrollmentPartialPhoneWithBlankSessionId() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 25)
    public void testVerifySearchForEnrollmentPartialPhoneAndDob() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone and dob case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
    }

    @Test(priority = 26)
    public void testVerifySearchForEnrollmentPartialPhoneAndDobWithInvalidSessionId() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone and dob with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 27)
    public void testVerifySearchForEnrollmentPartialPhoneAndDobWithBlankSessionId() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for enrollment partial phone and dob with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 28)
    public void testVerifySearchForViaHicn() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForViaHicnEndpoint, memberSearchSouthernPDF2020SessionId) + dataProp.getProperty("SearchForViaHicnData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for via hicn case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 29)
    public void testVerifySearchForViaHicnWithInvalidSessionId() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForViaHicnEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForViaHicnData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for via hicn with invalid SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 30)
    public void testVerifySearchForViaHicnWithBlankSessionId() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForViaHicnEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForViaHicnData"));
        ExtentLogger.pass("Test MemberSearchSouthernPDF2020SpecTest search for via hicn with blank SessionID case response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 404);
    }

    private String getMemberSearchSouthernPDF2020SessionId() {
        String memberSearchSouthernPDF2020SessionIdData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionMemberSearch.json";
        memberSearchSouthernPDF2020SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(memberSearchSouthernPDF2020SessionIdData));
        return memberSearchSouthernPDF2020SessionId;
    }
}