package tests;

import helpers.AuthToken;
import io.restassured.RestAssured;
import models.AuthResponseModel;
import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = "https://api.respublica.ru";
        RestAssured.basePath = "/api";
        AuthResponseModel responce = AuthToken.getAuthDataResponse("testu1450@gmail.com",
                "w%vRkEE4yoYc");
        System.setProperty("authToken", responce.getToken());
    }
}
