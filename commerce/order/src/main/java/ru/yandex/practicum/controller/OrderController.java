package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoOrder.ProductReturnRequest;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    @GetMapping
    public List<OrderDto> getAllUserOrder(@RequestParam String username) {
        return orderService.getAllUserOrder(username);
    }

    @Override
    @PutMapping
    public OrderDto create(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest) {
        return orderService.create(createNewOrderRequest);
    }

    @Override
    @PostMapping("/return")
    public OrderDto returnProduct(@Valid @RequestBody ProductReturnRequest productReturnRequest) {
        return orderService.returnProduct(productReturnRequest);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto processPayment(@RequestBody UUID orderID) {
        return orderService.processPayment(orderID);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody UUID orderID) {
        return orderService.paymentFailed(orderID);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto processDelivery(@RequestBody UUID orderID) {
        return orderService.processDelivery(orderID);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderID) {
        return orderService.deliveryFailed(orderID);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto processCompleted(@RequestBody UUID orderID) {
        return orderService.processCompleted(orderID);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody UUID orderID) {
        return orderService.calculateTotal(orderID);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody UUID orderID) {
        return orderService.calculateDelivery(orderID);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto collectOrder(@RequestBody UUID orderID) {
        return orderService.collectOrder(orderID);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto collectOrderFailed(@RequestBody UUID orderID) {
        return orderService.collectOrderFailed(orderID);
    }
}
