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
import static org.testng.Assert.assertEquals;
import api.tests.config.enums.*;

public class HealthCostBenefitsTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();

    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();
    private  String invalidErrorMessage;
    private String invalidErrorCode;
    private String healthCostBenefitsSessionId;
    private String postHealthCostBenefitsEndpoint;
    private String postHealthCostBenefitsData;
    private String getHealthCostBenefitsEndpoint;

    @Test
    public void testVerifyPostHealthCostBenefits() {
        commonMethods.usePropertyFileData("PC-API");
        commonMethods.usePropertyFileForEndpoints();
        postHealthCostBenefitsData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/postHealthCostBenefitsData.json";
        postHealthCostBenefitsEndpoint = prop.getProperty("Sessions_PostHealthCostBenefits").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                        String.format(postHealthCostBenefitsEndpoint, getHealthCostBenefitsSessionId()),
                fileReader.getTestJsonFile(postHealthCostBenefitsData));
        assertEquals(response.getStatusCode(), 204);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 1)
    public void testVerifyPostHealthCostBenefitsWithEmptyBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                        String.format(postHealthCostBenefitsEndpoint, healthCostBenefitsSessionId),
                CommonValues.BLANK_BODY_REQUEST.value);
        invalidErrorMessage = response.jsonPath().getString("Message");
        String getErrorHealthCostCategoriesMessage = response.jsonPath().getString("ModelState['healthCostBenefits.BenefitCategories'][0]") ;
        assertEquals(response.getStatusCode(), 400);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(getErrorHealthCostCategoriesMessage, PlanCompareErrorMessages.BENEFIT_CATEGORIES_FIELD_REQUIRED.value);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 2)
    public void testVerifyPostHealthCostBenefitsBlankSessionId() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                        String.format(postHealthCostBenefitsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(postHealthCostBenefitsData));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 3)
    public void testVerifyPostHealthCostBenefitsWithInvalidSessionId() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                        String.format(postHealthCostBenefitsEndpoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(postHealthCostBenefitsData));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 4)
    public void testVerifyGetHealthCostBenefits() {
        getHealthCostBenefitsEndpoint = prop.getProperty("Sessions_GetHealthCostBenefits").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("Sessions_CreateSession") +
                        String.format(getHealthCostBenefitsEndpoint, getHealthCostBenefitsSessionId()));
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("IsUseDefaultValues"), CommonValues.TRUE_VALUE.value);
        assertEquals(response.jsonPath().getString("PlanYearForDefault"), dataProp.getProperty("PLAN_YEAR_FOR_DEFAULT"));
        assertEquals(response.jsonPath().getString("BenefitCategories[0].BenefitCategoryID"), dataProp.getProperty("BENEFIT_CATEGORIES_BENEFIT_CATEGORY_ID"));
        assertEquals(response.jsonPath().getString("BenefitCategories[0].BenefitCategoryName"), dataProp.getProperty("BENEFIT_CATEGORIES_BENEFIT_CATEGORY_NAME"));
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 5)
    public void testVerifyGetHealthCostBenefitsWithBlankSessionId() {
        getHealthCostBenefitsEndpoint = prop.getProperty("Sessions_GetHealthCostBenefits").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("Sessions_CreateSession") +
                String.format(getHealthCostBenefitsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    @Test(priority = 6)
    public void testVerifyGetHealthCostBenefitsWithInvalidSessionId() {
        getHealthCostBenefitsEndpoint = prop.getProperty("Sessions_GetHealthCostBenefits").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("Sessions_CreateSession") +
                String.format(getHealthCostBenefitsEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        ExtentLogger.pass("Response : " + response.asPrettyString());
    }

    private String getHealthCostBenefitsSessionId(){
        String postCreateSessionData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/createSessionData.json";
        healthCostBenefitsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return healthCostBenefitsSessionId;
    }
}
