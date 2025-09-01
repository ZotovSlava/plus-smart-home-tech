package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_booking")
public class OrderBooking {
    @Id
    @GeneratedValue
    @Column(name = "order_booking_id", updatable = false, nullable = false)
    private UUID orderBookingId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_booking_id")
    private List<BookingProduct> bookingProducts;

    @Column(name = "order_id")
    private UUID orderId;


    @Column(name = "delivery_id")
    private UUID deliveryId;
}
