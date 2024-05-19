package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.SaleLineDto;
import com.mouad.stockmanagement.dto.StockMovementDto;
import com.mouad.stockmanagement.dto.SaleDto;

import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;

import com.mouad.stockmanagement.model.Product;
import com.mouad.stockmanagement.model.SaleLine;
import com.mouad.stockmanagement.model.StockMovementSource;
import com.mouad.stockmanagement.model.StockMovementType;
import com.mouad.stockmanagement.model.Sale;

import com.mouad.stockmanagement.repository.ProductRepository;
import com.mouad.stockmanagement.repository.SaleLineRepository;
import com.mouad.stockmanagement.repository.SaleRepository;

import com.mouad.stockmanagement.services.StockMovementService;
import com.mouad.stockmanagement.services.SaleService;

import com.mouad.stockmanagement.validator.SaleValidator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class SaleServiceImpl implements SaleService {
    private ProductRepository productRepository;
    private SaleRepository saleRepository;
    private SaleLineRepository saleLineRepository;
    private StockMovementService stockMovementService;

    @Autowired
    public SaleServiceImpl(ProductRepository productRepository,
                             SaleRepository saleRepository,
                             SaleLineRepository saleLineRepository,
                             StockMovementService stockMovementService) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.saleLineRepository = saleLineRepository;
        this.stockMovementService = stockMovementService;
    }

    @Override
    public SaleDto save(SaleDto dto) {
        List<String> errors = SaleValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Vente n'est pas valide");
            throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.SALE_NOT_VALID, errors);
        }

        List<String> productErrors = new ArrayList<>();

        dto.getSaleLines().forEach(saleLineDto -> {
            Optional<Product> article = productRepository.findById(saleLineDto.getProduct().getId());
            if (article.isEmpty()) {
                productErrors.add("Aucun article avec l'ID " + saleLineDto.getProduct().getId() + " n'a ete trouve dans la BDD");
            }
        });

        if (!productErrors.isEmpty()) {
            log.error("One or more products were not found in the DB, {}", errors);
            throw new InvalidEntityException("Un ou plusieurs articles n'ont pas ete trouve dans la BDD", ErrorCodes.SALE_NOT_VALID, errors);
        }

        Sale saleSaved = saleRepository.save(SaleDto.toEntity(dto));

        dto.getSaleLines().forEach(saleLineDto -> {
            SaleLine saleLine = SaleLineDto.toEntity(saleLineDto);
            saleLine.setSale(saleSaved);
            saleLineRepository.save(saleLine);
            updateStockMovement(saleLine);
        });
        return SaleDto.fromEntity(saleSaved);
    }

    @Override
    public SaleDto findById(Integer id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return null;
        }
        return saleRepository.findById(id)
            .map(SaleDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucun vente n'a ete trouve dans la BDD", ErrorCodes.SALE_NOT_FOUND));
    }

    @Override
    public SaleDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Vente CODE is NULL");
            return null;
        }
        return saleRepository.findSaleByCode(code)
            .map(SaleDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException(
                "Aucune vente client n'a ete trouve avec le CODE " + code, ErrorCodes.SALE_NOT_VALID
            ));
    }

    @Override
    public List<SaleDto> findAll() {
        return saleRepository.findAll().stream()
            .map(SaleDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Vente ID is NULL");
            return;
        }
        List<SaleLine> saleLine = saleLineRepository.findAllBySaleId(id);
        if (!saleLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une vente ...", ErrorCodes.SALE_ALREADY_IN_USE);
        }
        saleRepository.deleteById(id);
    }


    private void updateStockMovement(SaleLine lig) {
        StockMovementDto stockMovementDto = StockMovementDto.builder()
            .product(ProductDto.fromEntity(lig.getProduct()))
            .movementDate(Instant.now())
            .stockMovementType(StockMovementType.EXIT)
            .stockMovementSource(StockMovementSource.SALE)
            .quantity(lig.getQuantity())
            .companyId(lig.getCompanyId())
            .build();
        stockMovementService.exitStock(stockMovementDto);
    }
}
