package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress();

    @PutMapping("/api/v1/warehouse")
    void createProduct(@Valid @RequestBody ProductCreateDto productCreateDto);

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkAvailabilityProduct(@Valid @RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/api/v1/warehouse/add")
    void addProduct(@Valid @RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest);
}
