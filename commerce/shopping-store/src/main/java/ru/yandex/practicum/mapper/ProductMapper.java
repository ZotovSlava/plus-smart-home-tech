package ru.yandex.practicum.mapper;


import ru.yandex.practicum.dto.dtoShoppingStore.ProductCreateDto;
import ru.yandex.practicum.dto.dtoShoppingStore.ProductRequestDto;
import ru.yandex.practicum.model.Product;

public class ProductMapper {

    public static Product toEntity(ProductCreateDto productCreateDto) {
        return Product.builder()
                .productName(productCreateDto.getProductName())
                .description(productCreateDto.getDescription())
                .imageSrc(productCreateDto.getImageSrc())
                .quantityState(productCreateDto.getQuantityState())
                .productState(productCreateDto.getProductState())
                .productCategory(productCreateDto.getProductCategory())
                .price(productCreateDto.getPrice())
                .build();
    }

    public static ProductRequestDto toRequestDto(Product product) {
        return ProductRequestDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }
}
