package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.COMPANY_ENDPOINT;

import com.mouad.stockmanagement.dto.CompanyDto;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CompanyApi {

    @PostMapping(COMPANY_ENDPOINT + "/create")
    CompanyDto save(@RequestBody CompanyDto dto);

    @GetMapping(COMPANY_ENDPOINT + "/{companyId}")
    CompanyDto findById(@PathVariable("companyId") Integer id);

    @GetMapping(COMPANY_ENDPOINT + "/all")
    List<CompanyDto> findAll();

    @DeleteMapping(COMPANY_ENDPOINT + "/delete/{companyId}")
    void delete(@PathVariable("companyId") Integer id);

}
