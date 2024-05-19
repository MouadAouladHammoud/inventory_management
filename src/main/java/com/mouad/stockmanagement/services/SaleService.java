package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.SaleDto;
import java.util.List;

public interface SaleService {
    SaleDto save(SaleDto dto);
    SaleDto findById(Integer id);
    SaleDto findByCode(String code);
    List<SaleDto> findAll();
    void delete(Integer id);
}
