package models;

import lombok.Data;

@Data
public class WithMessageResponseModel {
    boolean success;
    String message;
    ActionsWithWish action;
}


