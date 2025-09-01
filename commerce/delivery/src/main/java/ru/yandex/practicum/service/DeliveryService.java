package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto create(DeliveryDto deliveryDto);

    void simulateSuccessfulDelivery(UUID orderID);

    void simulateTransferProductForDelivery(UUID orderID);

    void simulateFailedDelivery(UUID orderID);

    BigDecimal calculateDeliveryCost(OrderDto orderDto);
}
