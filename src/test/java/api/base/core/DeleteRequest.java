package api.base.core;

import api.base.reporter.ExtentLogger;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DeleteRequest {

    Response response;

    // Generic API Method for calling DELETE request
    public Response deleteMethod(String host, String accessToken, String apiUrl, String testData) {
        try {
            RestAssured.baseURI = host;
            RequestSpecification httpRequest = RestAssured.given().
                    header("Authorization", "Bearer " + accessToken);
            httpRequest.contentType(ContentType.JSON);
            httpRequest.body(testData);
            response = httpRequest.delete(apiUrl);
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
        return response;
    }
}