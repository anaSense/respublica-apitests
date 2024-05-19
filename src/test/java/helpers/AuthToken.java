package helpers;

import models.bodies.AuthBodyModel;
import models.responses.AuthResponseModel;
import models.bodies.UserBodyModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.*;

public class AuthToken {

    public static AuthResponseModel getAuthDataResponse(String email, String password) {
        AuthBodyModel model = new AuthBodyModel();
        UserBodyModel user = new UserBodyModel();
        user.setEmail(email);
        user.setPassword(password);
        model.setUser(user);

        AuthResponseModel response = step("Log in", () ->
                given(baseRequestSpec)
                        .body(model)
                        .when()
                        .post("/v1/users/login")
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(AuthResponseModel.class));
        assertThat(response.getToken()).isAlphanumeric().hasSizeGreaterThan(5);
        return response;
    }
}
