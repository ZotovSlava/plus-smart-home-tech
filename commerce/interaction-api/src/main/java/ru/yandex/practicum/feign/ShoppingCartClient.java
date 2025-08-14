package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
@RequestMapping("/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @GetMapping
    ShoppingCartDto getByUsername(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProduct(@RequestParam String username, @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivate(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProduct(@RequestParam String username, @RequestBody List<UUID> productsID);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantityProduct(@RequestParam String username,
                                          @RequestBody ChangeProductQuantityRequest request);
}
