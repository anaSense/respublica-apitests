package models.responses;

import lombok.Data;

@Data
public class ErrorResponseModel {
    String status, error;
}
