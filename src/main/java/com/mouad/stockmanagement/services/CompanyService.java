package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.CompanyDto;
import java.util.List;

public interface CompanyService {
    CompanyDto save(CompanyDto dto);
    CompanyDto findById(Integer id);
    List<CompanyDto> findAll();
    void delete(Integer id);
}
