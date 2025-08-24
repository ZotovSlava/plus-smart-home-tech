package ru.yandex.practicum.dto.dtoOrder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.AddressDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNewOrderRequest {
    @NotNull
    private ShoppingCartDto shoppingCartDto;

    @NotNull
    private AddressDto addressDto;
}
