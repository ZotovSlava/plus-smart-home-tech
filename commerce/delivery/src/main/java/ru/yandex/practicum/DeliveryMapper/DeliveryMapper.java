package ru.yandex.practicum.DeliveryMapper;

import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryState;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;

public class DeliveryMapper {
    public static Delivery toEntity(DeliveryDto deliveryDto, Address fromAddress, Address toAddress) {
        return Delivery.builder()
                .deliveryState(DeliveryState.CREATED)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .orderId(deliveryDto.getOrderId())
                .build();
    }

    public static DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryState(delivery.getDeliveryState())
                .from(AddressMapper.toRequestDto(delivery.getFromAddress()))
                .to(AddressMapper.toRequestDto(delivery.getToAddress()))
                .OrderId(delivery.getOrderId())
                .build();
    }
}
