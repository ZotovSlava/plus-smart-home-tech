package ru.yandex.practicum.mapper;


import ru.yandex.practicum.dto.dtoWarehouse.ProductCreateDto;
import ru.yandex.practicum.model.ProductCatalog;

public class ProductMapper {

    public static ProductCatalog toEntity(ProductCreateDto productCreateDto) {
        return ProductCatalog.builder()
                .productId(productCreateDto.getProductId())
                .depth(productCreateDto.getDimension().getDepth())
                .width(productCreateDto.getDimension().getWidth())
                .height(productCreateDto.getDimension().getHeight())
                .weight(productCreateDto.getWeight())
                .fragile(productCreateDto.getFragile())
                .build();
    }
}
