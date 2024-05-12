package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.SaleLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleLineRepository extends JpaRepository<SaleLine, Integer> {
    List<SaleLine> findAllByProductId(Integer productId);
    List<SaleLine> findAllBySaleId(Integer id);
}