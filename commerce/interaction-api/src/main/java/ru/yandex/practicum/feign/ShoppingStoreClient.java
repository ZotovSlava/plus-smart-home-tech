package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingStore.*;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    Page<ProductRequestDto> getByType(@RequestParam("category") ProductCategory productCategory,
                                      Pageable pageable);

    @GetMapping("/{productId}")
    ProductRequestDto getById(@PathVariable UUID productId);

    @PutMapping
    ProductRequestDto create(@RequestBody ProductCreateDto productCreateDto);

    @PostMapping
    ProductRequestDto update(@RequestBody ProductUpdateDto productUpdateDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeFromStore(@RequestBody UUID productID);

    @PostMapping("/quantityState")
    Boolean updateQuantityState(@RequestParam UUID productId,
                                @RequestParam QuantityState quantityState);
}
