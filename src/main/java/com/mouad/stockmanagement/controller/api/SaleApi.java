package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.SALES_ENDPOINT;

import com.mouad.stockmanagement.dto.SaleDto;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SaleApi {
    @PostMapping(SALES_ENDPOINT + "/create")
    SaleDto save(@RequestBody SaleDto dto);

    @GetMapping(SALES_ENDPOINT + "/{saleId}")
    SaleDto findById(@PathVariable("saleId") Integer id);

    @GetMapping(SALES_ENDPOINT + "/{saleCode}")
    SaleDto findByCode(@PathVariable("saleCode") String code);

    @GetMapping(SALES_ENDPOINT + "/all")
    List<SaleDto> findAll();

    @DeleteMapping(SALES_ENDPOINT + "/delete/{saleId}")
    void delete(@PathVariable("saleId") Integer id);
}
