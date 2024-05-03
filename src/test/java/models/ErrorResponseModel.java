package models;

import lombok.Data;

@Data
public class ErrorResponseModel {
    String status, error;
}
