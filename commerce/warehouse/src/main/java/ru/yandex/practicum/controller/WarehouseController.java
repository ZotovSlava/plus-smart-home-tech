package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/warehouse")
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @Override
    @PutMapping
    public void createProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        warehouseService.createProduct(productCreateDto);
    }

    @Override
    @PostMapping("/check")
    public BookedProductsDto checkAvailabilityProduct(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkAvailabilityProduct(shoppingCartDto);
    }

    @Override
    @PostMapping("/add")
    public void addProduct(@Valid @RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest) {
        warehouseService.addProduct(addProductToWarehouseRequest);
    }

    @Override
    @PostMapping("/shipped")
    public void shippedOrder(@Valid @RequestBody ShippedToDeliveryRequest shippedToDeliveryRequest){
        warehouseService.shippedOrder(shippedToDeliveryRequest);
    }

    @Override
    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<UUID, Integer> products){
        warehouseService.returnProducts(products);
    }

    @Override
    @PostMapping("/assembly")
    public BookedProductsDto collectOrder(@Valid @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest){
        return warehouseService.collectOrder(assemblyProductsForOrderRequest);
    };
}
