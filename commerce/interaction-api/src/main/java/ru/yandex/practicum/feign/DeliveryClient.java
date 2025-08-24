package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery")
@RequestMapping("/api/v1/delivery")
public interface DeliveryClient {
    @PutMapping
    DeliveryDto create(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void simulateSuccessfulDelivery(@RequestBody UUID orderID);

    @PostMapping("/picked")
    void simulateTransferProductForDelivery(@RequestBody UUID orderID);

    @PostMapping("/failed")
    void simulateFailedDelivery(@RequestBody UUID orderID);

    @PostMapping("/cost")
    BigDecimal calculateDeliveryCost(@RequestBody OrderDto orderDto);
}
