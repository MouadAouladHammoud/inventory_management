package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}