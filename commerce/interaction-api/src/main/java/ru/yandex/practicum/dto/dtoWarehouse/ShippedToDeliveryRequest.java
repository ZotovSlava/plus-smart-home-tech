package ru.yandex.practicum.dto.dtoWarehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippedToDeliveryRequest {
    @NotNull(message = "Order id can not be null")
    private UUID orderId;
    @NotNull(message = "Delivery id can not be null")
    private UUID deliveryId;
}
