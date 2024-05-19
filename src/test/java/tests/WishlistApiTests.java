package tests;

import data.ErrorMessages;
import io.qameta.allure.*;
import models.*;
import models.responses.WishlistResponseModel;
import models.responses.BaseResponseWithMessageModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.*;

@Feature("Wishlist feature")
@Story("Endpoint GET /v1/account/wishes/add_or_remove_wish_item/<item_id>")
@Owner("egorovaa")
public class WishlistApiTests extends TestBase {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Successful addition of item to wishlist")
    void successfullyAddItemToWishlistTest() {
        String validItemId = constantsProperties.getProperty("validItemId");
        boolean isExist = checkIsItemWithIdExistInWishlist(validItemId);
        if (isExist)
            prepareForAddRemoveWishlistActions(validItemId, "add");

        BaseResponseWithMessageModel response = step(format("Add item with id %s "
                + "to wishlist", validItemId), () ->
                given(baseRequestSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + validItemId)
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(BaseResponseWithMessageModel.class));

        step("Сheck that the request to add item to wishlist was successful", () -> {
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getAction()).isEqualTo(ActionsWithWish.ADD_TO_WISHLIST);
        });

        boolean isExistAfterAdd = step("Get all items from wishlist", () -> checkIsItemWithIdExistInWishlist(validItemId));

        step(format("Сheck that item with id %s is in wishlist", validItemId), () -> assertThat(isExistAfterAdd).isTrue());
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Successful removal of item from wishlist")
    void successfullyRemoveItemFromWishlistTest() {
        String validItemId2 = constantsProperties.getProperty("validItemId2");
        boolean isExist = checkIsItemWithIdExistInWishlist(validItemId2);
        if (!isExist)
            prepareForAddRemoveWishlistActions(validItemId2, "remove");

        BaseResponseWithMessageModel response = step(format("Remove item with id %s "
                + "from wishlist", validItemId2), () ->
                given(baseRequestSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + validItemId2)
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(BaseResponseWithMessageModel.class));

        step("Сheck that the request to remove item from wishlist was successful", () -> {
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getAction()).isEqualTo(ActionsWithWish.REMOVE_FROM_WISHLIST);
        });

        boolean isExistAfterAdd = step("Get all items from wishlist", () -> checkIsItemWithIdExistInWishlist(validItemId2));

        step(format("Сheck that item with id %s is not in wishlist", validItemId2), () -> assertThat(isExistAfterAdd).isFalse());
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Failed to add item with non-existent id to wishlist")
    void failedAddToWishlistWithNonexistentItemIdTest() {
        String invalidItemId = constantsProperties.getProperty("invalidItemId");
        BaseResponseWithMessageModel errorResponseModel = step(format("Try to add item with id %s "
                + "to wishlist", invalidItemId), () ->
                given(baseRequestSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + invalidItemId)
                        .then()
                        .statusCode(404)
                        .spec(baseResponseLoggerSpec)
                        .extract().as(BaseResponseWithMessageModel.class));

        step("Сheck that the error was shown", () -> {
            assertThat(errorResponseModel.isSuccess()).isFalse();
            assertThat(errorResponseModel.getMessage()).isEqualTo(ErrorMessages.ITEM_NOT_FOUND_ERROR.getMessage());
        });
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check the response schema for successfully adding an item to the wishlist")
    void checkSuccessAddToWishlistSchemaTest() {
        step("Check schema of request", () ->
                given(baseRequestSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + constantsProperties.getProperty("validItemId2"))
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath("schemas/success_add_remove_wishlist.json")));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Failed to add item to wishlist without token")
    void failedAddToWishlistWithoutTokenTest() {
        String validItemId = constantsProperties.getProperty("validItemId");
        BaseResponseWithMessageModel errorResponseModel = step(format("Try to add item with id %s " + "to wishlist", validItemId), () ->
                given(baseRequestSpec)
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + validItemId)
                        .then()
                        .statusCode(401)
                        .spec(baseResponseLoggerSpec)
                        .extract().as(BaseResponseWithMessageModel.class));

        step("Сheck that the unauthorized error was shown", () -> {
            assertThat(errorResponseModel.isSuccess()).isFalse();
            assertThat(errorResponseModel.getMessage()).isEqualTo(ErrorMessages.UNAUTHORIZED_ERROR.getMessage());
        });
    }

    private boolean checkIsItemWithIdExistInWishlist(String id) {
        return step(format("Prepare for testing, find item %s in the wishlist", id), () -> {
            List<ItemModel> items = new ArrayList<ItemModel>();
            int page = 1;
            while (true) {
                WishlistResponseModel response = given(baseRequestSpec)
                        .params("page", page)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes")
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(WishlistResponseModel.class);
                items.addAll(response.getItemDataModel().getItems());
                page++;
                if (response.getPagination().getNext() == 0) break;
            }
            return items.stream()
                    .anyMatch(item -> item.getId().equals(id));
        });
    }

    private void prepareForAddRemoveWishlistActions(String item, String action) {
        step(format("Prepare for testing, %s item %s wishlist if it exist", action, item), () ->
                given(baseRequestSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + item)
                        .then()
                        .spec(baseSuccessResponseSpec));
    }
}

