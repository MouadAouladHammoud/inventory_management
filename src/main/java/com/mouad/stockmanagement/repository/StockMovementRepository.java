package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.StockMovement;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {
    @Query("select sum(m.quantity) from StockMovement m where m.product.id = :productId")
    BigDecimal inventoryRealProduct(@Param("productId") Integer productId);
    List<StockMovement> findAllByProductId(Integer productId);
}
