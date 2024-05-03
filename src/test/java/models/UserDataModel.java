package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDataModel {
    @JsonProperty("data")
    UserModel userData;
}
