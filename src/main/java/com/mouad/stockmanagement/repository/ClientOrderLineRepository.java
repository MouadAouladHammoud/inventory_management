package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.ClientOrderLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOrderLineRepository extends JpaRepository<ClientOrderLine, Integer> {
    List<ClientOrderLine> findAllByClientOrderId(Integer id);
    List<ClientOrderLine> findAllByProductId(Integer id);
}