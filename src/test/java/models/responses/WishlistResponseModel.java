package models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import models.ItemDataModel;
import models.PaginationModel;

@Data
public class WishlistResponseModel {
    boolean success;
    @JsonProperty("items")
    ItemDataModel itemDataModel;
    @JsonProperty("pagination")
    PaginationModel pagination;
}
