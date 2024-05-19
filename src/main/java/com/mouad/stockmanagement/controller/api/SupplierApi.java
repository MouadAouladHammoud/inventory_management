package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.SUPPLIER_ENDPOINT;

import com.mouad.stockmanagement.dto.SupplierDto;
// import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Api("suppliers")
public interface SupplierApi {
    @PostMapping(SUPPLIER_ENDPOINT + "/create")
    SupplierDto save(@RequestBody SupplierDto dto);

    @GetMapping(SUPPLIER_ENDPOINT + "/{supplierId}")
    SupplierDto findById(@PathVariable("supplierId") Integer id);

    @GetMapping(SUPPLIER_ENDPOINT + "/all")
    List<SupplierDto> findAll();

    @DeleteMapping(SUPPLIER_ENDPOINT + "/delete/{supplierId}")
    void delete(@PathVariable("supplierId") Integer id);
}
