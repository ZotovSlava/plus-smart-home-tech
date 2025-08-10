package ru.yandex.practicum.dto.dtoShoppingCart;

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
public class ShoppingCartDto {
    @NotNull(message = "Shopping cart id can not be null")
    private UUID shoppingCartId;

    @NotNull(message = "Products map cannot be null")
    private Map<UUID, Integer> productsMap;
}
