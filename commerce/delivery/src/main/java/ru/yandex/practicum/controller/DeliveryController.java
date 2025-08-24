package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    @PutMapping
    public DeliveryDto create(@RequestBody DeliveryDto deliveryDto) {
        return deliveryService.create(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    public void simulateSuccessfulDelivery(@RequestBody UUID orderID) {

    }

    @Override
    @PostMapping("/picked")
    public void simulateTransferProductForDelivery(@RequestBody UUID orderID) {

    }

    @Override
    @PostMapping("/failed")
    public void simulateFailedDelivery(@RequestBody UUID orderID) {

    }

    @Override
    @PostMapping("/cost")
    public BigDecimal calculateDeliveryCost(@RequestBody OrderDto orderDto) {
        return null;

    }
}
