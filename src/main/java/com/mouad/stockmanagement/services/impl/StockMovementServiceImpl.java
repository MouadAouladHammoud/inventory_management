package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.StockMovementDto;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.model.StockMovementType;
import com.mouad.stockmanagement.repository.StockMovementRepository;
import com.mouad.stockmanagement.services.ProductService;
import com.mouad.stockmanagement.services.StockMovementService;
import com.mouad.stockmanagement.validator.StockMovementValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockMovementServiceImpl implements StockMovementService {
    private StockMovementRepository repository;
    private ProductService productService;
    @Autowired
    public StockMovementServiceImpl(StockMovementRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public BigDecimal inventoryRealProduct(Integer productId) {
        if (productId == null) {
            log.warn("Product ID is NULL");
            return BigDecimal.valueOf(-1);
        }
        productService.findById(productId);
        return repository.inventoryRealProduct(productId);
    }

    @Override
    public List<StockMovementDto> StockMovementProduct(Integer productId) {
        return repository.findAllByProductId(productId).stream()
            .map(StockMovementDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public StockMovementDto entryStock(StockMovementDto dto) {
        return  adjustStockPositive(dto, StockMovementType.ENTRY);
    }

    @Override
    public StockMovementDto exitStock(StockMovementDto dto) {
        return adjustStockNegative(dto, StockMovementType.EXIT);
    }

    @Override
    public StockMovementDto adjustStockPos(StockMovementDto dto) {
        return adjustStockPositive(dto, StockMovementType.ADJUST_POS);
    }

    @Override
    public StockMovementDto adjustStockNeg(StockMovementDto dto) {
        return adjustStockNegative(dto, StockMovementType.ADJUST_NEG);
    }

    private StockMovementDto adjustStockPositive(StockMovementDto dto, StockMovementType stockMovementType) {
        List<String> errors = StockMovementValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Product is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.STOCK_MOVEMENT_NOT_VALID, errors);
        }
        dto.setQuantity(
            BigDecimal.valueOf(
                Math.abs(dto.getQuantity().doubleValue())
            )
        );
        dto.setStockMovementType(stockMovementType);
        return StockMovementDto.fromEntity(
            repository.save(StockMovementDto.toEntity(dto))
        );
    }

    private StockMovementDto adjustStockNegative(StockMovementDto dto, StockMovementType stockMovementType) {
        List<String> errors = StockMovementValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.STOCK_MOVEMENT_NOT_VALID, errors);
        }
        dto.setQuantity(
            BigDecimal.valueOf(
                Math.abs(dto.getQuantity().doubleValue()) * -1
            )
        );
        dto.setStockMovementType(stockMovementType);
        return StockMovementDto.fromEntity(
            repository.save(StockMovementDto.toEntity(dto))
        );
    }
}
