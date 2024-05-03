package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAttributesModel {
    @JsonProperty("email")
    String email;
    @JsonProperty("wishlist_id")
    int wishlistId;
    @JsonProperty("email_verified")
    boolean emailVerified;
    @JsonProperty("name")
    String name;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;
    @JsonProperty("birthday")
    String birthday;
    @JsonProperty("is_subscribed")
    boolean isSubscribed;
    @JsonProperty("phone")
    String phone;
    @JsonProperty("phone_verified")
    boolean phoneVerified;
    @JsonProperty("role")
    int role;
    @JsonProperty("orders_count")
    int ordersCount;
    @JsonProperty("wishes_count")
    int wishesCount;
    @JsonProperty("reviews_count")
    int reviewsCount;
    @JsonProperty("wishlist_shared")
    boolean wishlistShared;
    @JsonProperty("cart_id")
    long cartId;
    @JsonProperty("receivers")
    ReceiverDataModel receiverModel;
}
