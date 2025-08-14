package ru.yandex.practicum.dto.dtoShoppingStore;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {
    @NotBlank(message = "ProductName can not be blank")
    private String productName;

    @NotBlank(message = "Description can not be blank")
    private String description;


    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;


    private ProductCategory productCategory;

    @NotNull(message = "Price can not be null")
    @DecimalMin(value = "1.0", message = "Price must be at least 1")
    private Double price;
}

