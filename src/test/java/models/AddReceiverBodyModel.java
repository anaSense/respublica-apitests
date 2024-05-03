package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddReceiverBodyModel {
    @JsonProperty("receiver")
    ReceiverAttributesModel receiverAttributesModel;
}
