package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final String userMessage;

    public PaymentNotFoundException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
    }

    public PaymentNotFoundException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }
}
