package ru.yandex.practicum.DeliveryMapper;

import ru.yandex.practicum.dto.dtoWarehouse.AddressDto;
import ru.yandex.practicum.model.Address;

public class AddressMapper {
    public static Address toEntity(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }

    public static AddressDto toRequestDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }
}
