package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.dtoShoppingStore.*;

import java.util.UUID;

public interface ShoppingStoreService {
    Page<ProductRequestDto> getByType(ProductCategory productCategory, Pageable pageable);

    ProductRequestDto getById(UUID productId);

    ProductRequestDto create(ProductCreateDto productCreateDto);

    ProductRequestDto update(ProductUpdateDto productUpdateDto);

    Boolean removeFromStore(UUID productId);

    Boolean updateQuantityState(UUID productId, QuantityState quantityState);
}
