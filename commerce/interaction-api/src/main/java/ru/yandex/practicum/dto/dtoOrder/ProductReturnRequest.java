package ru.yandex.practicum.dto.dtoOrder;

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
public class ProductReturnRequest {
    private UUID orderId;

    @NotNull
    private Map<UUID, Integer> products;
}
