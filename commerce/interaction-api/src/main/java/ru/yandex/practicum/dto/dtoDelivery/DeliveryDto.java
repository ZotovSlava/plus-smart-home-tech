package ru.yandex.practicum.dto.dtoDelivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.dtoWarehouse.AddressDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto from;
    private AddressDto to;
    private UUID OrderId;
    private DeliveryState deliveryState;
}
