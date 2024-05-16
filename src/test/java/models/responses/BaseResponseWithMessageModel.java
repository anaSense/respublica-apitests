package models.responses;

import lombok.Data;
import models.ActionsWithWish;

@Data
public class BaseResponseWithMessageModel {
    boolean success;
    String message;
    ActionsWithWish action;
}


