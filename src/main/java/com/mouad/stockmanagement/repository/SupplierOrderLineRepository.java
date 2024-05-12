package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.SupplierOrderLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierOrderLineRepository extends JpaRepository<SupplierOrderLine, Integer> {
    List<SupplierOrderLine> findAllBySupplierOrderId(Integer companyId);
    List<SupplierOrderLine> findAllByProductId(Integer companyId);
}
