package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.StockMovementDto;
import java.math.BigDecimal;
import java.util.List;

public interface StockMovementService {
    BigDecimal inventoryRealProduct(Integer productId);
    List<StockMovementDto> StockMovementProduct(Integer productId);
    StockMovementDto entryStock(StockMovementDto dto);
    StockMovementDto exitStock(StockMovementDto dto);
    StockMovementDto adjustStockPos(StockMovementDto dto);
    StockMovementDto adjustStockNeg(StockMovementDto dto);
}
