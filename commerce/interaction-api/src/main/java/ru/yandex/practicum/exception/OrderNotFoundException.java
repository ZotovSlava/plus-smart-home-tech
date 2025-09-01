package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String userMessage;

    public OrderNotFoundException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
    }

    public OrderNotFoundException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }
}
