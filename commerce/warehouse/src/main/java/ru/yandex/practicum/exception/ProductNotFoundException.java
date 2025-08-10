package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public ProductNotFoundException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public ProductNotFoundException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.NOT_FOUND;;
    }
}
