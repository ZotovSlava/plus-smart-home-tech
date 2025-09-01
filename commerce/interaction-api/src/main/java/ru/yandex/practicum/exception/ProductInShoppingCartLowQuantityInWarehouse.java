package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.BAD_REQUEST;;
    }
}
