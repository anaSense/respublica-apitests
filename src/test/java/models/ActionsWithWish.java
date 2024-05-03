package models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionsWithWish {
    ADD_TO_WISHLIST("add"),
    REMOVE_FROM_WISHLIST("remove");

    private final String value;
    @JsonValue
    public String getValue() {
        return value;
    }
}