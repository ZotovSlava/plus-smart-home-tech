package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopping_cart_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItem {
    @EmbeddedId
    private ShoppingCartItemId id;

    @Column(nullable = false)
    private Integer quantity;

    @MapsId("shoppingCartId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart cart;
}
