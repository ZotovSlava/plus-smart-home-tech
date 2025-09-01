package ru.yandex.practicum.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NoSpecifiedProductInWarehouseException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInCart(SpecifiedProductAlreadyInWarehouseException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().toString())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ResponseEntity<ErrorResponse> handleShoppingCartNotFound(ProductInShoppingCartLowQuantityInWarehouse ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShoppingCartNotFound(ProductNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorized(NotAuthorizedUserException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInCart(NoProductsInShoppingCartException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().toString())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShoppingCartNotFound(ShoppingCartNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(ShoppingCartNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryNotFound(ShoppingCartNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .userMessage(ex.getUserMessage())
                .httpStatus(ex.getHttpStatus().name())
                .stackTrace(mapStackTrace(ex.getStackTrace()))
                .cause(buildCauseMap(ex.getCause()))
                .suppressed(buildSuppressedList(ex.getSuppressed()))
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    private List<StackTraceElementDto> mapStackTrace(StackTraceElement[] stackTrace) {
        if (stackTrace == null) return null;
        return Arrays.stream(stackTrace)
                .map(StackTraceElementDto::new)
                .collect(Collectors.toList());
    }

    private Map<String, Object> buildCauseMap(Throwable cause) {
        if (cause == null) return null;

        Map<String, Object> causeMap = new LinkedHashMap<>();
        causeMap.put("message", cause.getMessage());
        causeMap.put("localizedMessage", cause.getLocalizedMessage());
        causeMap.put("stackTrace", mapStackTrace(cause.getStackTrace()));

        return causeMap;
    }

    private List<Map<String, Object>> buildSuppressedList(Throwable[] suppressed) {
        if (suppressed == null || suppressed.length == 0) return null;

        List<Map<String, Object>> suppressedList = new ArrayList<>();
        for (Throwable s : suppressed) {
            Map<String, Object> suppressedMap = new LinkedHashMap<>();
            suppressedMap.put("message", s.getMessage());
            suppressedMap.put("localizedMessage", s.getLocalizedMessage());
            suppressedMap.put("stackTrace", mapStackTrace(s.getStackTrace()));

            suppressedList.add(suppressedMap);
        }
        return suppressedList;
    }
}
