package data;

public enum ErrorMessages {
    ITEM_NOT_FOUND_ERROR("Товар не найден"),
    BAD_REQUEST_ERROR("Bad Request"),
    UNAUTHORIZED_ERROR("Невалидный токен");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
