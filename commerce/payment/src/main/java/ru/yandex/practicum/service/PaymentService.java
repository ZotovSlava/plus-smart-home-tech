package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoPayment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto processPayment(OrderDto orderDto);

    BigDecimal calculateTotalCost(OrderDto orderDto);

    void processPaymentSuccess(UUID paymentID);

    BigDecimal calculateProductCost(OrderDto orderDto);

    void processPaymentFailed(UUID paymentID);
}
