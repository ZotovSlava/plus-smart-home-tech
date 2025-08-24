package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.dtoOrder.CreateNewOrderRequest;
import ru.yandex.practicum.dto.dtoOrder.OrderState;
import ru.yandex.practicum.dto.dtoWarehouse.AddressDto;
import ru.yandex.practicum.dto.dtoWarehouse.BookedProductsDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderAddress;

public class AddressMapper {
    public static OrderAddress toEntity(AddressDto addressDto) {
        return OrderAddress.builder()
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }
}
