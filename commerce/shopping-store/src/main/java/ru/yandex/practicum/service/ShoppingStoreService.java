package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;

import ru.yandex.practicum.dto.dtoShoppingStore.ProductCategory;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductCreateDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductRequestDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {
    List<ProductRequestDto> getByType(ProductCategory productCategory, Pageable pageable);

    ProductRequestDto getById(UUID productId);

    ProductRequestDto create(ProductCreateDto productCreateDto);

    ProductRequestDto update(ProductUpdateDto productUpdateDto);

    Boolean removeFromStore(ProductUpdateDto productUpdateDto);

    Boolean updateQuantityState(ProductUpdateDto productUpdateDto);
}
