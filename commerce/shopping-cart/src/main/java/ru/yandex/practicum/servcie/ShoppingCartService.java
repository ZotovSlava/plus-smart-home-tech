package ru.yandex.practicum.servcie;

import ru.yandex.practicum.dto.dtoShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    public String getNameById(UUID shoppingCartId);

    ShoppingCartDto getByUsername(String username);

    ShoppingCartDto addProduct(String username, Map<UUID, Integer> products);

    void deactivate(String username);

    ShoppingCartDto removeProduct(String username, List<UUID> productsID);

    ShoppingCartDto changeQuantityProduct(String username, ChangeProductQuantityRequest changeProductQuantityRequest);
}
