package tests;

import config.AuthConfig;
import config.BaseConfigProvider;
import helpers.AuthToken;
import helpers.PropertyReader;
import models.responses.AuthResponseModel;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

import java.util.Properties;


public class TestBase {

    static Properties constantsProperties = new Properties();

    @BeforeAll
    public static void beforeAll() {
        PropertyReader.readPropertyFile(constantsProperties, "test_data/testData.properties");

        BaseConfigProvider configProvider = new BaseConfigProvider();
        configProvider.configure();
        AuthConfig authConfig = ConfigFactory.create(AuthConfig.class, System.getProperties());

        AuthResponseModel responce = AuthToken.getAuthDataResponse(authConfig.email(), authConfig.password());
        System.setProperty("authToken", responce.getToken());
    }
}
