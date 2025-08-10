package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItemId implements Serializable {
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "product_id")
    private UUID productId;
}
