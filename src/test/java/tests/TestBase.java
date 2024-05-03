package tests;

import helpers.AuthToken;
import io.restassured.RestAssured;
import models.AuthResponseModel;
import org.junit.jupiter.api.BeforeAll;

import static helpers.AuthTestDataConstants.AUTH_EMAIL;
import static helpers.AuthTestDataConstants.AUTH_PASSWORD;


public class TestBase {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = "https://api.respublica.ru";
        RestAssured.basePath = "/api";
        AuthResponseModel responce = AuthToken.getAuthDataResponse(AUTH_EMAIL,
                AUTH_PASSWORD);
        System.setProperty("authToken", responce.getToken());
    }
}
