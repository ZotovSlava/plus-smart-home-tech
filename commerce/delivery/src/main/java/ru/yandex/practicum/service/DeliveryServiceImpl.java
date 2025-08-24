package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.DeliveryMapper.AddressMapper;
import ru.yandex.practicum.DeliveryMapper.DeliveryMapper;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryDto;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryState;
import ru.yandex.practicum.dto.dtoOrder.OrderDto;
import ru.yandex.practicum.dto.dtoWarehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.DeliveryNotFoundException;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.storage.AddressRepository;
import ru.yandex.practicum.storage.DeliveryRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    public static final Double BASE_DELIVERY_COST = 5.0;

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto create(DeliveryDto deliveryDto) {

        Address addressFrom = addressRepository.save(
                AddressMapper.toEntity(
                        deliveryDto.getFrom()
                )
        );

        Address addressTo = addressRepository.save(
                AddressMapper.toEntity(
                        deliveryDto.getTo()
                )
        );

        return DeliveryMapper.toDto(
                deliveryRepository.save(
                        DeliveryMapper.toEntity(deliveryDto, addressFrom, addressTo
                        )
                )
        );
    }

    @Override
    public void simulateSuccessfulDelivery(UUID orderID) {
        Delivery delivery = deliveryRepository.findByOrderId(orderID)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found exception"));

        delivery.setDeliveryState(DeliveryState.DELIVERED);

        deliveryRepository.save(delivery);
    }

    @Override
    public void simulateTransferProductForDelivery(UUID orderID) {
        Delivery delivery = deliveryRepository.findByOrderId(orderID)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found exception"));

        warehouseClient.shippedOrder(ShippedToDeliveryRequest.builder()
                .orderId(orderID)
                .deliveryId(delivery.getDeliveryId())
                .build());

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);

        deliveryRepository.save(delivery);
    }

    @Override
    public void simulateFailedDelivery(UUID orderID) {
        Delivery delivery = deliveryRepository.findByOrderId(orderID)
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found exception"));

        delivery.setDeliveryState(DeliveryState.FAILED);

        deliveryRepository.save(delivery);
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        double deliveryCost = BASE_DELIVERY_COST;

        Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                .orElseThrow(() -> new DeliveryNotFoundException("Delivery not found exception"));

        Address address = delivery.getFromAddress();

        if ("ADDRESS_1".equals(delivery.getFromAddress().getCity())) {
            deliveryCost += BASE_DELIVERY_COST * 1;
        }

        if ("ADDRESS_2".equals(delivery.getFromAddress().getCity())) {
            deliveryCost += BASE_DELIVERY_COST * 2;
        }

        if (orderDto.getFragile()) {
            deliveryCost += deliveryCost * 0.2;
        }

        deliveryCost += orderDto.getDeliveryWeight() * 0.3;
        deliveryCost += orderDto.getDeliveryVolume() * 0.2;

        if (delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            return new BigDecimal(deliveryCost);
        }

        deliveryCost += deliveryCost * 0.2;

        return new BigDecimal(deliveryCost);
    }
}
