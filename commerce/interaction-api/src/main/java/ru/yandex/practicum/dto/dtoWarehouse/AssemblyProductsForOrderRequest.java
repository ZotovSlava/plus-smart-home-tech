package ru.yandex.practicum.dto.dtoWarehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssemblyProductsForOrderRequest {
    @NotNull(message = "Order id can not be null")
    UUID orderId;

    @NotNull(message = "Products map cannot be null")
    Map<UUID, Integer> products;
}
