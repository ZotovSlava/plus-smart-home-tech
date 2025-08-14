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
@Table(name = "product_catalog")
public class ProductCatalog {
    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID productId;

    @Column
    private Boolean fragile;

    @Column(nullable = false)
    private Double width;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double depth;

    @Column(nullable = false)
    private Double weight;

    @OneToOne(mappedBy = "productCatalog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WarehouseStock warehouseStock;
}
