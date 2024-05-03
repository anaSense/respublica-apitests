package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetWishlistResponseModel {
    boolean success;
    @JsonProperty("items")
    ItemDataModel itemDataModel;
    @JsonProperty("pagination")
    PaginationModel pagination;
}
