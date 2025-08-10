package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductCategory;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductCreateDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductRequestDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductUpdateDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store")
@RequestMapping("/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping
    List<ProductRequestDto> getByType(@RequestParam("category") ProductCategory productCategory,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{productId}")
    ProductRequestDto getById(@PathVariable UUID productId);

    @PutMapping
    ProductRequestDto create(@RequestBody ProductCreateDto productCreateDto);

    @PostMapping
    ProductRequestDto update(@RequestBody ProductUpdateDto productUpdateDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeFromStore(@RequestBody ProductUpdateDto productUpdateDto);

    @PostMapping("/quantityState")
    Boolean updateQuantityState(@RequestBody ProductUpdateDto productUpdateDto);
}
