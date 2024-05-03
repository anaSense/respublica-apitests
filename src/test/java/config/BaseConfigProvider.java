package config;

import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;

public class BaseConfigProvider {
    static BaseConfig baseConfig = ConfigFactory.create(BaseConfig.class, System.getProperties());

    public void configure() {
        RestAssured.baseURI = baseConfig.baseUrl();
        RestAssured.basePath = baseConfig.basePath();
    }
}
