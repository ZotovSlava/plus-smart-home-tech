package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public NoSpecifiedProductInWarehouseException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NoSpecifiedProductInWarehouseException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
