package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoPayment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;


@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto processPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/totalCost")
    BigDecimal calculateTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/refund")
    void processPaymentSuccess(@RequestBody UUID paymentID);

    @PostMapping("/productCost")
    BigDecimal calculateProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/failed")
    void processPaymentFailed(@RequestBody UUID paymentID);
}
