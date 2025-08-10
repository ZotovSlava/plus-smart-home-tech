package ru.yandex.practicum.mapper;


import ru.yandex.practicum.dto.dtoWarehouse.*;
import ru.yandex.practicum.model.ProductCatalog;

public class ProductMapper {

    public static ProductCatalog toEntity(ProductCreateDto productCreateDto) {
        return ProductCatalog.builder()
                .productId(productCreateDto.getProductId())
                .depth(productCreateDto.getDimensionDto().getDepth())
                .width(productCreateDto.getDimensionDto().getWidth())
                .height(productCreateDto.getDimensionDto().getHeight())
                .weight(productCreateDto.getWeight())
                .fragile(productCreateDto.getFragile())
                .build();
    }
}
