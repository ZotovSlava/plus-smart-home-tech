package ru.yandex.practicum.dto.dtoShoppingStore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDto {
    @NotNull(message = "ProductId can not be null")
    private UUID productId;

    private String productName;

    private String description;

    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;

    private ProductCategory productCategory;

    @DecimalMin(value = "1.0", message = "Price must be at least 1")
    private Double price;
}
