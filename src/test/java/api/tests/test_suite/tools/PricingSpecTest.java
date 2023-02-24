package api.tests.test_suite.tools;

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

public class PricingSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    @Test(priority = 0)
    public void testPricingForSingleCase() {
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        String pricingForNameEndpoint = prop.getProperty("Pricing_Calculate");
        String pricingForNameData = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/singlePricing.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE"),
                fileReader.getTestJsonFile(pricingForNameData));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull("APTCValue");
    }

    @Test(priority = 1)
    public void testPricingForNameFamily() {
        String pricingForNameEndpoint = prop.getProperty("Pricing_Calculate");
        String pricingForFamily = System.getProperty("user.dir") + "/src/test/resources/api.testdata/api.testdata.tools/famPricing.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE"),
                fileReader.getTestJsonFile(pricingForFamily));
        assertEquals(response.getStatusCode(), 200);
        assertNotNull("APTCValue");
    }
}