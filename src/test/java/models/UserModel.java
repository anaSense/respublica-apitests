package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {
    @JsonProperty("id")
    String id;
    @JsonProperty("type")
    String type;
    @JsonProperty("attributes")
    UserAttributesModel attributes;
}
