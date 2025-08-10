package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.dtoShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.dtoWarehouse.*;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.ProductCatalog;
import ru.yandex.practicum.model.WarehouseStock;
import ru.yandex.practicum.storage.ProductCatalogRepository;
import ru.yandex.practicum.storage.WarehouseStockRepository;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductCatalogRepository productCatalogRepository;
    private final WarehouseStockRepository warehouseStockRepository;


    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS;

    static {
        Random random = new Random();
        int index = random.nextInt(2);
        CURRENT_ADDRESS = ADDRESSES[index];
    }

    @Override
    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    @Override
    public void createProduct(ProductCreateDto productCreateDto) {
        productCatalogRepository.findById(productCreateDto.getProductId())
                .ifPresent(product -> {
                    throw new SpecifiedProductAlreadyInWarehouseException("Товар уже добавлен в каталог");
                });

        productCatalogRepository.save(ProductMapper.toEntity(productCreateDto));
    }

    @Override
    public BookedProductsDto checkAvailabilityProduct(ShoppingCartDto shoppingCartDto) {
        boolean fragile = false;
        double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;

        for (Map.Entry<UUID, Integer> entry : shoppingCartDto.getProductsMap().entrySet()) {
            UUID key = entry.getKey();
            Integer value = entry.getValue();

            ProductCatalog productCatalog = productCatalogRepository.findById(key).orElseThrow();
            WarehouseStock warehouseStock = warehouseStockRepository.findById(key).orElseThrow();

            if (warehouseStock.getQuantity() < value) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара на складе недостаточно");
            }

            if (productCatalog.getFragile()) {
                fragile = true;
            }

            double volume = productCatalog.getDepth() * productCatalog.getHeight() * productCatalog.getWidth();
            deliveryVolume += volume;

            double weight = productCatalog.getWeight();
            deliveryWeight += weight;
        }

        return BookedProductsDto.builder()
                .fragile(fragile)
                .deliveryVolume(deliveryVolume)
                .deliveryWeight(deliveryWeight)
                .build();
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        ProductCatalog productCatalog = productCatalogRepository.findById(addProductToWarehouseRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Такого продукта нет в каталоге"));

        WarehouseStock warehouseStock = WarehouseStock.builder()
                .productCatalog(productCatalog)
                .quantity(addProductToWarehouseRequest.getQuantity())
                .build();

        warehouseStockRepository.save(warehouseStock);
    }
}
