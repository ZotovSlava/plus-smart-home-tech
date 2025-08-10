package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductCategory;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductCreateDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductRequestDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductUpdateDto;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    public List<ProductRequestDto> getByType( @RequestParam("category") ProductCategory productCategory,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
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
    public Boolean removeFromStore(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        return shoppingStoreService.removeFromStore(productUpdateDto);
    }

    @Override
    @PostMapping("/quantityState")
    public Boolean updateQuantityState(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        return shoppingStoreService.updateQuantityState(productUpdateDto);
    }
}
