package ru.yandex.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.ProductCatalog;

import java.util.UUID;

@Repository
public interface ProductCatalogRepository extends JpaRepository<ProductCatalog, UUID> {
}
