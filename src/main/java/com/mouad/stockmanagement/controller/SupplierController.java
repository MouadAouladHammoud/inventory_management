package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.SupplierApi;
import com.mouad.stockmanagement.dto.SupplierDto;
import com.mouad.stockmanagement.services.SupplierService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierController implements SupplierApi {
    private SupplierService supplierService;
    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public SupplierDto save(SupplierDto dto) {
        return supplierService.save(dto);
    }

    @Override
    public SupplierDto findById(Integer id) {
        return supplierService.findById(id);
    }

    @Override
    public List<SupplierDto> findAll() {
        return supplierService.findAll();
    }

    @Override
    public void delete(Integer id) {
        supplierService.delete(id);
    }
}
