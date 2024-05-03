package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemDataModel {
    @JsonProperty("data")
    List<ItemModel> items;
}
