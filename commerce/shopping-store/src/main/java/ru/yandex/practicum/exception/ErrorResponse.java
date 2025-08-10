package ru.yandex.practicum.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private String userMessage;
    private String httpStatus;
    private List<StackTraceElementDto> stackTrace;
    private Map<String, Object> cause;
    private List<Map<String, Object>> suppressed;
}
