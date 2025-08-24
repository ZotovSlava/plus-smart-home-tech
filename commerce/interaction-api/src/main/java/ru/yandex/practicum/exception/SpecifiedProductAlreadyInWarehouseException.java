package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String userMessage;

    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
        this.userMessage = message;
    }

    public SpecifiedProductAlreadyInWarehouseException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }
}
