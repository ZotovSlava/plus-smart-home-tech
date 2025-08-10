package ru.yandex.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.feign.ShoppingCartClient;
import ru.yandex.practicum.servcie.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/shopping-car")
public class SoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    public ShoppingCartDto getByUsername(@RequestParam String username) {
        return shoppingCartService.getByUsername(username);
    }

    @Override
    @PutMapping
    public ShoppingCartDto addProduct(@RequestParam String username, @RequestBody Map<UUID, Integer> products) {
        return shoppingCartService.addProduct(username, products);
    }

    @Override
    @DeleteMapping
    public void deactivate(@RequestParam String username) {
        shoppingCartService.deactivate(username);
    }

    @Override
    @PostMapping("/remove")
    public ShoppingCartDto removeProduct(@RequestParam String username, @RequestBody List<UUID> productsID) {
        return shoppingCartService.removeProduct(username, productsID);
    }

    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantityProduct(@RequestParam String username,
                                                 @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest) {
        return shoppingCartService.changeQuantityProduct(username, changeProductQuantityRequest);
    }
}
