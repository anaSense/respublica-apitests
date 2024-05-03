package tests;

import config.AuthConfig;
import config.BaseConfigProvider;
import helpers.AuthToken;
import io.restassured.RestAssured;
import models.responses.AuthResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;


public class TestBase {

    static private BaseConfigProvider configProvider = new BaseConfigProvider();

    @BeforeAll
    public static void beforeAll() {
        configProvider.configure();
        AuthConfig authConfig = ConfigFactory.create(AuthConfig.class, System.getProperties());

        AuthResponseModel responce = AuthToken.getAuthDataResponse(authConfig.email(),
                authConfig.password());
        System.setProperty("authToken", responce.getToken());
    }
}
