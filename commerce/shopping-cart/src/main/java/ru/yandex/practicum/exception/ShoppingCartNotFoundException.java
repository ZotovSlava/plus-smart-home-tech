package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShoppingCartNotFoundException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public ShoppingCartNotFoundException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public ShoppingCartNotFoundException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.NOT_FOUND;;
    }
}
