package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoPayment.PaymentDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductRequestDto;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exception.PaymentNotFoundException;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.storage.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public static final BigDecimal BASE_TAX = BigDecimal.valueOf(0.1);

    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    public PaymentDto processPayment(OrderDto orderDto) {
        if (orderDto.getProductPrice() == null || orderDto.getDeliveryPrice() == null || orderDto.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info in order to calculate exception");
        }

        Payment payment = Payment.builder()
                .totalPayment(orderDto.getTotalPrice())
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(orderDto.getProductPrice().multiply(BASE_TAX))
                .orderId(orderDto.getOrderId())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        BigDecimal productsPrice = orderDto.getProductPrice();
        BigDecimal deliveryPrice = orderDto.getDeliveryPrice();
        BigDecimal feePrice = productsPrice.multiply(BASE_TAX);

        return productsPrice.add(deliveryPrice).add(feePrice);
    }

    @Override
    public void processPaymentSuccess(UUID paymentID) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        Map<UUID, Integer> productsMap = orderDto.getProducts();
        BigDecimal productsCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Integer> entry : productsMap.entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            ProductRequestDto productRequestDto = shoppingStoreClient.getById(productId);
            double priceDouble = productRequestDto.getPrice();
            BigDecimal price = BigDecimal.valueOf(priceDouble);

            productsCost = productsCost.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        return productsCost;
    }

    @Override
    public void processPaymentFailed(UUID paymentID) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.FAILED);

        paymentRepository.save(payment);
    }
}
