package tests;

import io.qameta.allure.*;
import models.*;
import models.responses.AuthResponseModel;
import models.responses.ErrorResponseModel;
import models.responses.WithMessageResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import utils.RandomUtils;

import java.util.List;

import static data.TestDataConstants.BAD_REQUEST_ERROR;
import static data.TestDataConstants.UNAUTHORIZED_ERROR;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.*;

@Feature("Add receiver to profile feature")
@Story("Endpoint POST /v1/account/receivers")
@Owner("egorovaa")
public class AddReceiverApiTests extends TestBase {

    private final RandomUtils randomUtils = new RandomUtils();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Successful addition of receiver to user's profile")
    void successfullyAddReceiverTest() {
        String email = randomUtils.getEmail();
        String name = randomUtils.getName();
        String phone = randomUtils.getPhoneNumber();
        String title = randomUtils.getTitle();

        AddReceiverBodyModel model = fillBodyModelByValid(email, name, phone, title);

        AuthResponseModel response = step("Add new receiver to profile", () -> {
            AuthResponseModel responseModel =
                    given(baseRequestSpec)
                            .body(model)
                            .header("JWT-Auth-Token", System.getProperty("authToken"))
                            .when()
                            .post("/v1/account/receivers")
                            .then().spec(baseSuccessResponseSpec)
                            .extract().as(AuthResponseModel.class);
            assertThat(responseModel.isSuccess()).isTrue();
            return responseModel;
        });

        step("Check that the new receiver was added to the user's profile", () -> {
            List<ReceiverModel> receivers = response.getUser().getUserData()
                    .getAttributes().getReceiverModel().getReceivers();
            assertThat(receivers.size()).isNotZero();
            boolean isExist = false;
            for (ReceiverModel receiver : receivers) {
                ReceiverAttributesModel receiverAttributes = receiver.getAttributes();
                if (receiverAttributes.getEmail().equals(email)
                        && receiverAttributes.getName().equals(name)
                        && receiverAttributes.getPhone().equals(phone)
                        && receiverAttributes.getTitle().equals(title)) {
                    isExist = true;
                    break;
                }
            }
            assertThat(isExist).isTrue();
        });
    }

    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest(name = "Failed to add new receiver with empty combination of body fields " + "email = {0}, name = {1}, phone = {2}, title = {3}")
    @CsvFileSource(resources = "/test_data/pairwiseTestsFailedAddReceiver.csv", delimiter = '|')
    void failedAddReceiverWithEmptyBodyFields(String email, String name, String phone, String title, String errorText) {
        AddReceiverBodyModel model = fillBodyModelByValid(email, name, phone, title);

        WithMessageResponseModel response = step("Try to add new receiver to profile", () ->
                given(baseRequestSpec).body(model)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .post("/v1/account/receivers")
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(WithMessageResponseModel.class));

        step("Check the error was shown " + errorText, () -> {
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo(errorText);
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Failed to add receiver to user's profile with null body")
    void failedAddReceiverWithWrongBodyFields() {
        AddReceiverBodyModel model = new AddReceiverBodyModel();

        ErrorResponseModel response = step("Try to add new receiver to profile", () ->
                given(baseRequestSpec)
                        .body(model)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .post("/v1/account/receivers")
                        .then()
                        .spec(baseResponseWithBadRequestErrorSpec)
                        .extract().as(ErrorResponseModel.class));

        step("Check the error was shown ", () -> {
            assertThat(response.getError()).isEqualTo(BAD_REQUEST_ERROR);
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Failed to add receiver to user's profile without token")
    void failedAddReceiverWithoutTokenFields() {
        String email = randomUtils.getEmail();
        String name = randomUtils.getName();
        String phone = randomUtils.getPhoneNumber();
        String title = randomUtils.getTitle();

        AddReceiverBodyModel model = fillBodyModelByValid(email, name, phone, title);

        WithMessageResponseModel response = step("Try to add new receiver to profile", () ->
                given(baseRequestSpec)
                        .body(model)
                        .when()
                        .post("/v1/account/receivers")
                        .then()
                        .spec(baseResponseUnauthorizedErrorSpec)
                        .extract().as(WithMessageResponseModel.class));

        step("Check the unauthorized error was shown ", () -> {
            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getMessage()).isEqualTo(UNAUTHORIZED_ERROR);
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check the response schema for successfully adding a receiver to the profile")
    void checkSuccessAddReceiverSchemaTest() {
        String email = randomUtils.getEmail();
        String name = randomUtils.getName();
        String phone = randomUtils.getPhoneNumber();
        String title = randomUtils.getTitle();
        AddReceiverBodyModel model = fillBodyModelByValid(email, name, phone, title);

        step("Check schema of request", () ->
                given(baseRequestSpec)
                        .body(model)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .post("/v1/account/receivers")
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/success_add_receiver.json")));
    }

    private AddReceiverBodyModel fillBodyModelByValid(String email, String name, String phone, String title) {
        AddReceiverBodyModel model = new AddReceiverBodyModel();
        ReceiverAttributesModel receiverModel = new ReceiverAttributesModel();
        receiverModel.setEmail(email);
        receiverModel.setName(name);
        receiverModel.setPhone(phone);
        receiverModel.setTitle(title);
        model.setReceiverAttributesModel(receiverModel);
        return model;
    }
}
