package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.CategoryDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface CategoryService {
    CategoryDto save(CategoryDto dto);
    CategoryDto findById(Integer id,  Integer userId);
    CategoryDto findByCode(String code);
    List<CategoryDto> findAll();
    void delete(Integer id);
}
