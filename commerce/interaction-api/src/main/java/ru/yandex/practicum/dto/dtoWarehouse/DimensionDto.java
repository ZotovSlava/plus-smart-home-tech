package ru.yandex.practicum.dto.dtoWarehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DimensionDto {
    @NotNull(message = "Width can not be null")
    @DecimalMin(value = "1.0", message = "Width must be at least 1")
    private Double width;

    @NotNull(message = "Height can not be null")
    @DecimalMin(value = "1.0", message = "Height must be at least 1")
    private Double height;

    @NotNull(message = "Depth can not be null")
    @DecimalMin(value = "1.0", message = "Depth must be at least 1")
    private Double depth;
}
