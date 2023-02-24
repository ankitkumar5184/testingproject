package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class CountiesSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    @Test
    public void testSingleCountyRequestSCS30467(){
        commonMethods.usePropertyFileData("TOOLS-API");
        commonMethods.usePropertyFileForEndpoints();
        String countiesEndPoint = prop.getProperty("Counties_Groovy");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), countiesEndPoint + ZipCodeValues.Zip_92129.value);
        assertEquals(response.jsonPath().getString("[0].CountyName"), dataProp.getProperty("COUNTY_SAN_DIEGO"));
        assertEquals(response.jsonPath().getString("[0].State"), dataProp.getProperty("STATE_CA"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void testSingleCountyRequest(){
        String countiesEndPoint = prop.getProperty("Counties_Groovy");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), countiesEndPoint + ZipCodeValues.ZIP_90010.value);
        assertEquals(response.jsonPath().getString("[0].CountyName"), dataProp.getProperty("COUNTY_LOS_ANGLES"));
        assertEquals(response.jsonPath().getString("[0].State"), dataProp.getProperty("STATE_CA"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testMultiCountyRequest(){
        String countiesEndPoint = prop.getProperty("Counties_Groovy");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), countiesEndPoint + ZipCodeValues.ZIP_27713.value);
        assertEquals(response.jsonPath().getString("[0].CountyName"),dataProp.getProperty("COUNTY_CHATHAM"));
        assertEquals(response.jsonPath().getString("[1].CountyName"),dataProp.getProperty("COUNTY_DURHAM"));
        assertEquals(response.jsonPath().getString("[2].CountyName"),dataProp.getProperty("COUNTY_WAKE"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testInvalidCountyRequest(){
        String countiesEndPoint = prop.getProperty("Counties_Groovy");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), countiesEndPoint + ZipCodeValues.ZIP_45.value);
        assertEquals(response.getStatusCode(), 200);
    }
}