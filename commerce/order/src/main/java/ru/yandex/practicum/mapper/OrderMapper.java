package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoOrder.OrderState;
import ru.yandex.practicum.dto.dtoWarehouse.BookedProductsDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderMapper {
    public static Order toEntity(CreateNewOrderRequest createNewOrderRequest, BookedProductsDto bookedProductsDto) {
        return Order.builder()
                .shoppingCartId(createNewOrderRequest.getShoppingCartDto().getShoppingCartId())
                .state(OrderState.NEW)
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .fragile(bookedProductsDto.getFragile())
                .build();
    }

    public static OrderDto toDto(Order order, CreateNewOrderRequest createNewOrderRequest) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(createNewOrderRequest.getShoppingCartDto().getShoppingCartId())
                .products(createNewOrderRequest.getShoppingCartDto().getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    public static OrderDto toDto2(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(getProductsMap(order.getProducts()))
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    private static Map<UUID, Integer> getProductsMap(List<OrderItem> orderItems) {
        Map<UUID, Integer> products = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            products.put(orderItem.getProductId(), orderItem.getQuantity());
        }

        return products;
    }
}
