package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.model.ClientOrderLine;
import com.mouad.stockmanagement.model.SupplierOrderLine;
import com.mouad.stockmanagement.model.SaleLine;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.SaleLineDto;

import com.mouad.stockmanagement.repository.ProductRepository;
import com.mouad.stockmanagement.repository.ClientOrderLineRepository;
import com.mouad.stockmanagement.repository.SupplierOrderLineRepository;
import com.mouad.stockmanagement.repository.SaleLineRepository;

import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;

import com.mouad.stockmanagement.services.ProductService;

import com.mouad.stockmanagement.validator.ProductValidator;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private SaleLineRepository saleLineRepository;
    private SupplierOrderLineRepository supplierOrderLineRepository;
    private ClientOrderLineRepository clientOrderLineRepository;

    @Autowired
    public ProductServiceImpl(
        ProductRepository productRepository,
        SaleLineRepository saleLineRepository, SupplierOrderLineRepository supplierOrderLineRepository,
        ClientOrderLineRepository clientOrderLineRepository) {
        this.productRepository = productRepository;
        this.saleLineRepository = saleLineRepository;
        this.supplierOrderLineRepository = supplierOrderLineRepository;
        this.clientOrderLineRepository = clientOrderLineRepository;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        List<String> errors = ProductValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.PRODUCT_NOT_VALID, errors);
        }

        return ProductDto.fromEntity(
                productRepository.save(
                ProductDto.toEntity(dto)
            )
        );
    }

    @Override
    public ProductDto findById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }

        return productRepository.findById(id).map(ProductDto::fromEntity).orElseThrow(() ->
            new EntityNotFoundException("Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD", ErrorCodes.PRODUCT_NOT_FOUND)
        );
    }

    @Override
    public ProductDto findProductsByCode(String productCode) {
        if (!StringUtils.hasLength(productCode)) {
            log.error("Article CODE is null");
            return null;
        }

        return productRepository.findProductByProductCode(productCode)
            .map(ProductDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucun article avec le CODE = " + productCode + " n' ete trouve dans la BDD", ErrorCodes.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
            .map(ProductDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SaleLineDto> findSales(Integer productId) {
        return saleLineRepository.findAllByProductId(productId).stream()
            .map(SaleLineDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<ClientOrderLineDto> findClientOrders(Integer productId) {
        return clientOrderLineRepository.findAllByProductId(productId).stream()
            .map(ClientOrderLineDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierOrderLineDto> findSupplierOrders(Integer productId) {
        return supplierOrderLineRepository.findAllByProductId(productId).stream()
            .map(SupplierOrderLineDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findAllProductsByIdCategory(Integer categoryId) {
        return productRepository.findAllByCategoryId(categoryId).stream()
            .map(ProductDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return;
        }
        List<ClientOrderLine> clientOrderLine = clientOrderLineRepository.findAllByProductId(id);
        if (!clientOrderLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.PRODUCT_ALREADY_IN_USE);
        }
        List<SupplierOrderLine> supplierOrderLine = supplierOrderLineRepository.findAllByProductId(id);
        if (!supplierOrderLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes fournisseur", ErrorCodes.PRODUCT_ALREADY_IN_USE);
        }
        List<SaleLine> saleLine = saleLineRepository.findAllByProductId(id);
        if (!saleLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes", ErrorCodes.PRODUCT_ALREADY_IN_USE);
        }
        productRepository.deleteById(id);
    }
}
