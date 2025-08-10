package com.example.products.repository;

import com.example.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    boolean existsByProductAndType(String product, String type);

    @Query("SELECT p FROM Product p WHERE p.product IN :products AND p.type IN :types")
    List<Product> findAllByProductAndTypeIn(@Param("products") List<String> products, @Param("types") List<String> types);
}
