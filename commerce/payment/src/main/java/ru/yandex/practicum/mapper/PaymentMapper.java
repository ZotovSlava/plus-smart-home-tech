package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.dtoPayment.PaymentDto;
import ru.yandex.practicum.model.Payment;

public class PaymentMapper {
    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .deliveryTotal(payment.getDeliveryTotal())
                .totalPayment(payment.getTotalPayment())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}
