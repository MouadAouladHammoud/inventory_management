package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findProductByProductCode(String productCode);
    List<Product> findAllByCategoryId(Integer categoryId);
}