package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoOrder.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderDto> getAllUserOrder(String username);

    OrderDto create(CreateNewOrderRequest createNewOrderRequest);

    OrderDto returnProduct(ProductReturnRequest productReturnRequest);

    OrderDto processPayment(UUID orderID);

    OrderDto paymentFailed(UUID orderID);

    OrderDto processDelivery(UUID orderID);

    OrderDto deliveryFailed(UUID orderID);

    OrderDto processCompleted(UUID orderID);

    OrderDto calculateTotal(UUID orderID);

    OrderDto calculateDelivery(UUID orderID);

    OrderDto collectOrder(UUID orderID);

    OrderDto collectOrderFailed(UUID orderID);
}
