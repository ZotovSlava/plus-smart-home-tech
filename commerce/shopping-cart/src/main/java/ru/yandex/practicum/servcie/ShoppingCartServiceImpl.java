package ru.yandex.practicum.servcie;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.dtoShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ShoppingCartNotFoundException;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;
import ru.yandex.practicum.model.ShoppingCartItemId;
import ru.yandex.practicum.storage.ShoppingCartItemRepository;
import ru.yandex.practicum.storage.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final WarehouseClient warehouseClient;

    @Override
    public String getNameById(UUID shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).get();

        return shoppingCart.getUsername();
    }

    @Override
    public ShoppingCartDto getByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("User not authorized");
        }

        return shoppingCartRepository.getByUsername(username)
                .map(cart -> ShoppingCartDto.builder()
                        .shoppingCartId(cart.getShoppingCartId())
                        .products(getProductQuantities(cart.getShoppingCartId()))
                        .build()
                )
                .orElseGet(() -> {
                    ShoppingCart newCart = shoppingCartRepository.save(
                            ShoppingCart.builder()
                                    .username(username)
                                    .isActive(true)
                                    .build()
                    );

                    return ShoppingCartDto.builder()
                            .shoppingCartId(newCart.getShoppingCartId())
                            .products(new HashMap<>())
                            .build();
                });
    }

    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Integer> products) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("User not authorized");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.getByUsername(username)
                .orElseGet(() -> {
                    return shoppingCartRepository.save(
                            ShoppingCart.builder()
                                    .username(username)
                                    .isActive(true)
                                    .build()
                    );
                });

        Map<UUID, Integer> productQuantities = getProductQuantities(shoppingCart.getShoppingCartId());

        productQuantities.forEach((key, value) ->
                products.merge(key, value, Integer::sum));

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(shoppingCart.getShoppingCartId(), products);

        warehouseClient.checkAvailabilityProduct(shoppingCartDto);

        products.forEach((key, value) -> {
            ShoppingCartItemId shoppingCartItemId = ShoppingCartItemId.builder()
                    .productId(key)
                    .shoppingCartId(shoppingCart.getShoppingCartId())
                    .build();

            ShoppingCartItem shoppingCartItem = ShoppingCartItem.builder()
                    .id(shoppingCartItemId)
                    .quantity(value)
                    .cart(shoppingCart)
                    .build();

            shoppingCartItemRepository.save(shoppingCartItem);
        });

        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(getProductQuantities(shoppingCart.getShoppingCartId()))
                .build();
    }

    @Override
    public void deactivate(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("User not authorized");
        }

        shoppingCartRepository.getByUsername(username)
                .map(shoppingCart -> {
                    shoppingCart.setIsActive(false);
                    return shoppingCartRepository.save(shoppingCart);
                })
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found for username: " + username));

    }

    @Override
    public ShoppingCartDto removeProduct(String username, List<UUID> productsID) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("User not authorized");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.getByUsername(username)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found for username: " + username));

        for (UUID id : productsID) {
            ShoppingCartItemId itemId = ShoppingCartItemId.builder()
                    .shoppingCartId(shoppingCart.getShoppingCartId())
                    .productId(id)
                    .build();

            shoppingCartItemRepository.findById(itemId)
                    .ifPresent(shoppingCartItemRepository::delete);
        }

        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(getProductQuantities(shoppingCart.getShoppingCartId()))
                .build();
    }

    @Override
    public ShoppingCartDto changeQuantityProduct(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("User not authorized");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.getByUsername(username)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found for username: " + username));

        ShoppingCartItemId shoppingCartItemId = ShoppingCartItemId.builder()
                .productId(changeProductQuantityRequest.getProductId())
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .build();

        ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findById(shoppingCartItemId)
                .orElseThrow(() -> new ShoppingCartNotFoundException("Shopping cart not found for username: " + username));

        shoppingCartItem.setQuantity(changeProductQuantityRequest.getNewQuantity());

        shoppingCartItemRepository.save(shoppingCartItem);

        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(getProductQuantities(shoppingCart.getShoppingCartId()))
                .build();
    }

    private Map<UUID, Integer> getProductQuantities(UUID cartId) {
        return shoppingCartItemRepository.findByCart_ShoppingCartId(cartId)
                .stream()
                .collect(Collectors.toMap(
                        i -> i.getId().getProductId(),
                        ShoppingCartItem::getQuantity
                ));
    }
}
