package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders_address")
public class OrderAddress {
    @Id
    @GeneratedValue
    @Column(name = "order_address_id", updatable = false, nullable = false)
    private UUID orderAddressId;

    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
