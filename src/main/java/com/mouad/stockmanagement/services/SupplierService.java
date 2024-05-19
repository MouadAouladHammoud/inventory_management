package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.SupplierDto;
import java.util.List;

public interface SupplierService {
    SupplierDto save(SupplierDto dto);
    SupplierDto findById(Integer id);
    List<SupplierDto> findAll();
    void delete(Integer id);
}
