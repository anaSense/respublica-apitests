package models.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import models.ReceiverAttributesModel;

@Data
public class ReceiverBodyModel {
    @JsonProperty("receiver")
    ReceiverAttributesModel receiverAttributesModel;
}
