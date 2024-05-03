package tests;

import io.qameta.allure.*;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static helpers.TestDataConstants.*;
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
    @DisplayName("Successfully add item to wishlist")
    void successfullyAddItemToWishlistTest() {
        boolean isExist = checkIsItemWithIdExistInWishlist(VALID_ITEM_ID);

        if(isExist) {
            step(format("Prepare for test, remove item %s from wishlist, if exist",
                    VALID_ITEM_ID), () ->
                given(baseRequestWithTokenSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID)
                        .then()
                        .spec(baseSuccessResponseSpec));
        }

        WithMessageResponseModel response = step(format("Add item with id %s " +
                    "to wishlist", VALID_ITEM_ID), () ->
                given(baseRequestWithTokenSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID)
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(WithMessageResponseModel.class));

            step("Сheck that the request to add item to wishlist was successful", () -> {
                        assertThat(response.isSuccess()).isTrue();
                        assertThat(response.getAction()).isEqualTo(ActionsWithWish.ADD_TO_WISHLIST);
            });

            boolean isExistAfterAdd = step("Get all items from wishlist", () ->
                    checkIsItemWithIdExistInWishlist(VALID_ITEM_ID));

        step(format("Сheck that item with id %s is in wishlist", VALID_ITEM_ID), () ->
                assertThat(isExistAfterAdd).isTrue());
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Successfully remove item from wishlist")
    void successfullyRemoveItemFromWishlistTest() {
        boolean isExist = checkIsItemWithIdExistInWishlist(VALID_ITEM_ID_2);

        if(!isExist) {
            step(format("Prepare for test, add item %s to wishlist, if no",
                    VALID_ITEM_ID_2), () ->
                    given(baseRequestWithTokenSpec)
                            .header("JWT-Auth-Token", System.getProperty("authToken"))
                            .when()
                            .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID_2)
                            .then()
                            .spec(baseSuccessResponseSpec));
        }

        WithMessageResponseModel response = step(format("Remove item with id %s " +
                "from wishlist", VALID_ITEM_ID_2), () ->
                given(baseRequestWithTokenSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID_2)
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .extract().as(WithMessageResponseModel.class));

        step("Сheck that the request to remove item from wishlist was successful", () -> {
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getAction()).isEqualTo(ActionsWithWish.REMOVE_FROM_WISHLIST);
        });

        boolean isExistAfterAdd = step("Get all items from wishlist", () ->
                checkIsItemWithIdExistInWishlist(VALID_ITEM_ID_2));

        step(format("Сheck that item with id %s is not in wishlist", VALID_ITEM_ID_2), () ->
                assertThat(isExistAfterAdd).isFalse());
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Failed to add item with non-existent id to wishlist")
    void failedAddToWishlistWithNonexistentItemIdTest() {
        WithMessageResponseModel errorResponseModel = step(format("Try to add item with id %s " +
                "to wishlist", INVALID_ITEM_ID), () ->
                given(baseRequestWithTokenSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + INVALID_ITEM_ID)
                        .then()
                        .spec(baseResponseNotFoundErrorSpec)
                        .extract().as(WithMessageResponseModel.class));

        step("Сheck that the error was shown", () -> {
            assertThat(errorResponseModel.isSuccess()).isFalse();
            assertThat(errorResponseModel.getMessage())
                    .isEqualTo(ITEM_NOT_FOUND_ERROR);
        });
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Check response schema for successfully add item to  wishlist")
    void checkSuccessAddToWishlistSchemaTest() {
        step("Check schema of request", () ->
                given(baseRequestWithTokenSpec)
                        .header("JWT-Auth-Token", System.getProperty("authToken"))
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID_2)
                        .then()
                        .spec(baseSuccessResponseSpec)
                        .body(matchesJsonSchemaInClasspath(
                                "schemas/success_add_remove_wishlist.json")));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Failed to add item to wishlist without token")
    void failedAddToWishlistWithoutTokenTest() {
        WithMessageResponseModel errorResponseModel = step(format("Try to add item with id %s " +
                "to wishlist", VALID_ITEM_ID), () ->
                given(baseRequestWithTokenSpec)
                        .when()
                        .get("/v1/account/wishes/add_or_remove_wish_item/" + VALID_ITEM_ID)
                        .then()
                        .spec(baseResponseUnauthorizedErrorSpec)
                        .extract().as(WithMessageResponseModel.class));

        step("Сheck that the unauthorized error was shown", () -> {
            assertThat(errorResponseModel.isSuccess()).isFalse();
            assertThat(errorResponseModel.getMessage())
                    .isEqualTo(UNAUTHORIZED_ERROR);
        });
    }

    private boolean checkIsItemWithIdExistInWishlist(String id) {
        return step(format("Prepare for test, find item %s from wishlist",
                id), () -> {
            List<ItemModel> items = new ArrayList<ItemModel>();
            int page = 1;
            while (true) {
                GetWishlistResponseModel response =
                        given(baseRequestWithTokenSpec)
                                .params("page", page)
                                .header("JWT-Auth-Token", System.getProperty("authToken"))
                                .when()
                                .get("/v1/account/wishes")
                                .then()
                                .spec(baseSuccessResponseSpec)
                                .extract().as(GetWishlistResponseModel.class);
                items.addAll(response.getItemDataModel().getItems());
                page++;
                if (response.getPagination().getNext() == 0)
                    break;
            }
            for(ItemModel item : items) {
                if(item.getId().equals(id))
                    return true;
            }
            return false;
        });
    }
}

