package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.CompanyApi;
import com.mouad.stockmanagement.dto.CompanyDto;
import com.mouad.stockmanagement.services.CompanyService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController implements CompanyApi {

    private CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDto save(CompanyDto dto) {
        return companyService.save(dto);
    }

    @Override
    public CompanyDto findById(Integer id) {
        return companyService.findById(id);
    }

    @Override
    public List<CompanyDto> findAll() {
        return companyService.findAll();
    }

    @Override
    public void delete(Integer id) {
        companyService.delete(id);
    }
}
