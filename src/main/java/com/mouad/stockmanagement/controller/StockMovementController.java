package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.StockMovementApi;
import com.mouad.stockmanagement.dto.StockMovementDto;
import com.mouad.stockmanagement.services.StockMovementService;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockMovementController implements StockMovementApi {
    private StockMovementService service;
    @Autowired
    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    @Override
    public BigDecimal inventoryRealProduct(Integer productId) {
        return service.inventoryRealProduct(productId);
    }

    @Override
    public List<StockMovementDto> StockMovementProduct(Integer productId) {
        return service.StockMovementProduct(productId);
    }

    @Override
    public StockMovementDto entryStock(StockMovementDto dto) {
        return service.entryStock(dto);
    }

    @Override
    public StockMovementDto exitStock(StockMovementDto dto) {
        return service.exitStock(dto);
    }

    @Override
    public StockMovementDto adjustStockPos(StockMovementDto dto) {
        return service.adjustStockPos(dto);
    }

    @Override
    public StockMovementDto adjustStockNeg(StockMovementDto dto) {
        return service.adjustStockNeg(dto);
    }
}
