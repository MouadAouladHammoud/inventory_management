package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryDto dto);
    CategoryDto findById(Integer id);
    CategoryDto findByCode(String code);
    List<CategoryDto> findAll();
    void delete(Integer id);
}
