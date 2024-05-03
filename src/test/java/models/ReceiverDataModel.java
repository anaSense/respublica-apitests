package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ReceiverDataModel {
    @JsonProperty("data")
    List<ReceiverModel> receivers;
}
