package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.dtoShoppingStore.*;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.storage.ShoppingStoreRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;

    @Override
    public List<ProductRequestDto> getByType(ProductCategory productCategory, Pageable pageable) {
        Page<Product> productsPage = shoppingStoreRepository.findAllByProductCategory(productCategory, pageable);
        return productsPage.map(ProductMapper::toRequestDto).toList();
    }

    @Override
    public ProductRequestDto getById(UUID productId) {
        Product product = shoppingStoreRepository.getByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return ProductMapper.toRequestDto(product);
    }

    @Override
    public ProductRequestDto create(ProductCreateDto productCreateDto) {
        return ProductMapper.toRequestDto(
                shoppingStoreRepository.save(
                        ProductMapper.toEntity(productCreateDto)
                )
        );
    }

    @Override
    public ProductRequestDto update(ProductUpdateDto productUpdateDto) {
        UUID productId = productUpdateDto.getProductId();

        Product product = shoppingStoreRepository.getByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (productUpdateDto.getProductName() != null)
            product.setProductName(productUpdateDto.getProductName());

        if (productUpdateDto.getDescription() != null)
            product.setDescription(productUpdateDto.getDescription());

        if (productUpdateDto.getImageSrc() != null)
            product.setImageSrc(productUpdateDto.getImageSrc());

        if (productUpdateDto.getQuantityState() != null)
            product.setQuantityState(productUpdateDto.getQuantityState());

        if (productUpdateDto.getProductState() != null)
            product.setProductState(productUpdateDto.getProductState());

        if (productUpdateDto.getProductCategory() != null)
            product.setProductCategory(productUpdateDto.getProductCategory());

        if (productUpdateDto.getPrice() != null)
            product.setPrice(productUpdateDto.getPrice());


        return ProductMapper.toRequestDto(
                shoppingStoreRepository.save(product)
        );
    }

    @Override
    public Boolean removeFromStore(ProductUpdateDto productUpdateDto) {
        UUID productId = productUpdateDto.getProductId();

        Product product = shoppingStoreRepository.getByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setProductState(ProductState.DEACTIVATE);

        shoppingStoreRepository.save(product);

        return true;
    }

    @Override
    public Boolean updateQuantityState(ProductUpdateDto productUpdateDto) {
        UUID productId = productUpdateDto.getProductId();

        Product product = shoppingStoreRepository.getByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setQuantityState(productUpdateDto.getQuantityState());

        shoppingStoreRepository.save(product);

        return true;
    }
}
