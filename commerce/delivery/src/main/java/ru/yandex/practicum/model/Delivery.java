package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.dtoDelivery.DeliveryState;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id", updatable = false, nullable = false)
    private UUID deliveryId;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryState deliveryState;

    @ManyToOne
    @JoinColumn(name = "from_address_id", nullable = false)
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id", nullable = false)
    private Address toAddress;
}
