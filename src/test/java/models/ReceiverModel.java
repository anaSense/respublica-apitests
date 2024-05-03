package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReceiverModel {
    @JsonProperty("id")
    String id;
    @JsonProperty("type")
    String type;
    @JsonProperty("attributes")
    ReceiverAttributesModel attributes;
}
