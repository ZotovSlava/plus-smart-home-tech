package ru.yandex.practicum.service;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    AddressDto getAddress();

    void createProduct(ProductCreateDto productCreateDto);

    BookedProductsDto checkAvailabilityProduct(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest addProductToWarehouseRequest);

    void shippedOrder(ShippedToDeliveryRequest shippedToDeliveryRequest);

    void returnProducts(Map<UUID, Integer> products);

    BookedProductsDto collectOrder(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);
}
