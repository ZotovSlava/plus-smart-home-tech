package ru.yandex.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.OrderAddress;

import java.util.UUID;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, UUID> {
}
