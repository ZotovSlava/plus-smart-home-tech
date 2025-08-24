package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;

import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/api/v1/warehouse/shipped")
    void shippedOrder(@Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest);

    @PostMapping("/api/v1/warehouse/return")
    void returnProducts(@RequestBody Map<UUID, Integer> products);

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto collectOrder(@Valid @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);
}
