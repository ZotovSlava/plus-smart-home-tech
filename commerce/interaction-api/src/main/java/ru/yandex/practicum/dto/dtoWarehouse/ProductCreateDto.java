package ru.yandex.practicum.dto.dtoWarehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
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
public class ProductCreateDto {
    @NotNull(message = "Product id can not be null")
    private UUID productId;

    private Boolean fragile;

    @Valid
    private DimensionDto dimension;

    @NotNull(message = "Weight can not be null")
    @DecimalMin(value = "1.0", message = "Weight must be at least 1")
    private Double weight;
}
