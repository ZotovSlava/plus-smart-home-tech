package ru.yandex.practicum.service;


import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;

public interface WarehouseService {
    AddressDto getAddress();

    void createProduct(ProductCreateDto productCreateDto);

    BookedProductsDto checkAvailabilityProduct(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest addProductToWarehouseRequest);
}
