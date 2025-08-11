package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingStore.*;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    public Page<ProductRequestDto> getByType(
            @RequestParam("category") ProductCategory productCategory,
            Pageable pageable) {
        return shoppingStoreService.getByType(productCategory, pageable);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductRequestDto getById(@PathVariable UUID productId) {
        return shoppingStoreService.getById(productId);
    }

    @Override
    @PutMapping
    public ProductRequestDto create(@Valid @RequestBody ProductCreateDto productCreateDto) {
        return shoppingStoreService.create(productCreateDto);
    }

    @Override
    @PostMapping
    public ProductRequestDto update(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        return shoppingStoreService.update(productUpdateDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public Boolean removeFromStore(@RequestBody UUID productID) {
        return shoppingStoreService.removeFromStore(productID);
    }

    @Override
    @PostMapping("/quantityState")
    public Boolean updateQuantityState(@RequestParam UUID productId,
                                       @RequestParam QuantityState quantityState) {
        return shoppingStoreService.updateQuantityState(productId, quantityState);
    }
}
