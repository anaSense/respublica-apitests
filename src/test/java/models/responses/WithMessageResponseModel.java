package models.responses;

import lombok.Data;
import models.ActionsWithWish;

@Data
public class WithMessageResponseModel {
    boolean success;
    String message;
    ActionsWithWish action;
}


