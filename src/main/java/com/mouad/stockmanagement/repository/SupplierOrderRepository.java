package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.ClientOrder;
import com.mouad.stockmanagement.model.SupplierOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierOrderRepository extends JpaRepository<SupplierOrder, Integer> {
    Optional<SupplierOrder> findSupplierOrderByCode(String code);
    List<ClientOrder> findAllBySupplierId(Integer id);
}

