package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Sale;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Optional<Sale> findSaleByCode(String code);
}