package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotAuthorizedUserException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public NotAuthorizedUserException(String userMessage) {
        super(userMessage);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }

    public NotAuthorizedUserException(String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }
}
