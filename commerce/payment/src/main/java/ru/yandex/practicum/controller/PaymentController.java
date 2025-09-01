package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoPayment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    PaymentDto processPayment(@RequestBody OrderDto orderDto) {
        return paymentService.processPayment(orderDto);
    }

    @PostMapping("/totalCost")
    BigDecimal calculateTotalCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateTotalCost(orderDto);
    }

    @PostMapping("/refund")
    void processPaymentSuccess(@RequestBody UUID paymentID) {
        paymentService.processPaymentSuccess(paymentID);
    }

    @PostMapping("/productCost")
    BigDecimal calculateProductCost(@RequestBody OrderDto orderDto) {
        return paymentService.calculateProductCost(orderDto);
    }

    @PostMapping("/failed")
    void processPaymentFailed(@RequestBody UUID paymentID) {
        paymentService.processPaymentFailed(paymentID);
    }
}
