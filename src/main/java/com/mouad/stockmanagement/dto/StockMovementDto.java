package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.StockMovement;
import com.mouad.stockmanagement.model.StockMovementSource;
import com.mouad.stockmanagement.model.StockMovementType;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockMovementDto {
    private Integer id;
    private Instant movementDate;
    private BigDecimal quantity;
    private ProductDto product;
    private StockMovementType stockMovementType;
    private StockMovementSource stockMovementSource;
    private Integer companyId;

    public static StockMovementDto fromEntity(StockMovement stockMovement) {
        if (stockMovement == null) {
            return null;
        }
        return StockMovementDto.builder()
            .id(stockMovement.getId())
            .movementDate(stockMovement.getMovementDate())
            .quantity(stockMovement.getQuantity())
            .product(ProductDto.fromEntity(stockMovement.getProduct()))
            .stockMovementType(stockMovement.getStockMovementType())
            .stockMovementSource(stockMovement.getStockMovementSource())
            .companyId(stockMovement.getCompanyId())
            .build();
    }

    public static StockMovement toEntity(StockMovementDto dto) {
        if (dto == null) {
            return null;
        }
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(dto.getId());
        stockMovement.setMovementDate(dto.getMovementDate());
        stockMovement.setQuantity(dto.getQuantity());
        stockMovement.setProduct(ProductDto.toEntity(dto.getProduct()));
        stockMovement.setStockMovementType(dto.getStockMovementType());
        stockMovement.setStockMovementSource(dto.getStockMovementSource());
        stockMovement.setCompanyId(dto.getCompanyId());
        return stockMovement;
    }
}
