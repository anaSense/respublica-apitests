package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import models.UserDataModel;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseModel {
    boolean success;
    String token;
    @JsonProperty("user")
    UserDataModel user;
}
