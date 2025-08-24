package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoProductsInShoppingCartException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String userMessage;

    public NoProductsInShoppingCartException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public NoProductsInShoppingCartException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }
}
