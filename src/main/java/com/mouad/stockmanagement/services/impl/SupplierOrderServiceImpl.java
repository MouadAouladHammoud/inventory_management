package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.SupplierOrderDto;
import com.mouad.stockmanagement.dto.SupplierDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.StockMovementDto;

import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;

import com.mouad.stockmanagement.model.*;

import com.mouad.stockmanagement.repository.*;

import com.mouad.stockmanagement.services.SupplierOrderService;
import com.mouad.stockmanagement.services.StockMovementService;

import com.mouad.stockmanagement.validator.ProductValidator;
import com.mouad.stockmanagement.validator.SupplierOrderValidator;

import java.math.BigDecimal;
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
public class SupplierOrderServiceImpl implements SupplierOrderService {

    private SupplierOrderRepository supplierOrderRepository;
    private SupplierOrderLineRepository supplierOrderLineRepository;
    private SupplierRepository supplierRepository;
    private ProductRepository productRepository;
    private StockMovementService stockMovementService;
    @Autowired
    public SupplierOrderServiceImpl(SupplierOrderRepository supplierOrderRepository,
                                    SupplierRepository supplierRepository,
                                    ProductRepository productRepository,
                                    SupplierOrderLineRepository supplierOrderLineRepository,
                                    StockMovementService stockMovementService) {
        this.supplierOrderRepository = supplierOrderRepository;
        this.supplierOrderLineRepository = supplierOrderLineRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
    }

    private SupplierOrderDto checkOrderStatus(Integer orderId) {
        SupplierOrderDto supplierOrderDto = findById(orderId);
        if (supplierOrderDto.isOrderDelivered()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }
        return supplierOrderDto;
    }

    private Optional<SupplierOrderLine> findSupplierOrderLine(Integer orderLineId) {
        Optional<SupplierOrderLine> supplierOrderLineOptional = supplierOrderLineRepository.findById(orderLineId);
        if (supplierOrderLineOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + orderLineId, ErrorCodes.SUPPLIER_ORDER_NOT_FOUND);
        }
        return supplierOrderLineOptional;
    }

    private void checkOrderId(Integer orderId) {
        if (orderId == null) {
            log.error("Commande fournisseur ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }
    }

    private void checkOrderLineId(Integer orderLineId) {
        if (orderLineId == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }
    }

    private void checkProductId(Integer productId, String msg) {
        if (productId == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }
    }

    private void updateStockMovement(Integer orderId) {
        List<SupplierOrderLine> supplierOrderLine = supplierOrderLineRepository.findAllBySupplierOrderId(orderId);
        supplierOrderLine.forEach(line -> {
            processStockExit(line);
        });
    }

    private void processStockExit(SupplierOrderLine lig) {
        StockMovementDto stockMovementDto = StockMovementDto.builder()
            .product(ProductDto.fromEntity(lig.getProduct()))
            .movementDate(Instant.now())
            .stockMovementType(StockMovementType.ENTRY)
            .stockMovementSource(StockMovementSource.ORDER_SUPPLIER)
            .quantity(lig.getQuantity())
            .companyId(lig.getCompanyId())
            .build();
        stockMovementService.entryStock(stockMovementDto);
    }

    @Override
    public SupplierOrderDto save(SupplierOrderDto dto) {
        List<String> errors = SupplierOrderValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.SUPPLIER_ORDER_NOT_VALID, errors);
        }

        if (dto.getId() != null && dto.isOrderDelivered()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }

        Optional<Supplier> supplier = supplierRepository.findById(dto.getSupplier().getId());
        if (supplier.isEmpty()) {
            log.warn("Fournisseur with ID {} was not found in the DB", dto.getSupplier().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID" + dto.getSupplier().getId() + " n'a ete trouve dans la BDD", ErrorCodes.SUPPLIER_NOT_FOUND);
        }

        List<String> productErrors = new ArrayList<>();

        if (dto.getSupplierOrderLines() != null) {
            dto.getSupplierOrderLines().forEach(ligCmdFrs -> {
                if (ligCmdFrs.getProduct() != null) {
                    Optional<Product> product = productRepository.findById(ligCmdFrs.getProduct().getId());
                    if (product.isEmpty()) {
                        productErrors.add("L'article avec l'ID " + ligCmdFrs.getProduct().getId() + " n'existe pas");
                    }
                } else {
                    productErrors.add("Impossible d'enregister une commande avec un aticle NULL");
                }
            });
        }

        if (!productErrors.isEmpty()) {
            log.warn("");
            throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.PRODUCT_NOT_FOUND, productErrors);
        }
        dto.setOrderDate(Instant.now());
        SupplierOrder supplierOrderSaved = supplierOrderRepository.save(SupplierOrderDto.toEntity(dto));

        if (dto.getSupplierOrderLines() != null) {
            dto.getSupplierOrderLines().forEach(ligCmdFrs -> {
                SupplierOrderLine supplierOrderLine = SupplierOrderLineDto.toEntity(ligCmdFrs);
                supplierOrderLine.setSupplierOrder(supplierOrderSaved);
                supplierOrderLine.setCompanyId(supplierOrderSaved.getCompanyId());
                SupplierOrderLine lineSaved = supplierOrderLineRepository.save(supplierOrderLine);
                processStockExit(lineSaved);
            });
        }

        return SupplierOrderDto.fromEntity(supplierOrderSaved);
    }

    @Override
    public SupplierOrderDto updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        checkOrderId(orderId);
        if (!StringUtils.hasLength(String.valueOf(orderStatus))) {
            log.error("L'etat de la commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }

        SupplierOrderDto supplierOrder = checkOrderStatus(orderId);
        supplierOrder.setOrderStatus(orderStatus);

        SupplierOrder supplierOrderSaved = supplierOrderRepository.save(SupplierOrderDto.toEntity(supplierOrder));
        if (supplierOrder.isOrderDelivered()) {
            updateStockMovement(orderId);
        }
        return SupplierOrderDto.fromEntity(supplierOrderSaved);
    }

    @Override
    public SupplierOrderDto updateOrderQuantity(Integer orderId, Integer orderLineId, BigDecimal quantity) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }

        SupplierOrderDto supplierOrder = checkOrderStatus(orderId);
        Optional<SupplierOrderLine> supplierOrderLineOptional = findSupplierOrderLine(orderLineId);

        SupplierOrderLine supplierOrderLine = supplierOrderLineOptional.get();
        supplierOrderLine.setQuantity(quantity);
        supplierOrderLineRepository.save(supplierOrderLine);
        return supplierOrder;
    }

    @Override
    public SupplierOrderDto updateSupplier(Integer orderId, Integer supplierId) {
        checkOrderId(orderId);
        if (supplierId == null) {
            log.error("L'ID du fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID fournisseur null", ErrorCodes.SUPPLIER_ORDER_NOT_MODIFIABLE);
        }

        SupplierOrderDto supplierOrder = checkOrderStatus(orderId);
        Optional<Supplier> supplierOptional = supplierRepository.findById(supplierId);
        if (supplierOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun fournisseur n'a ete trouve avec l'ID " + supplierId, ErrorCodes.SUPPLIER_NOT_FOUND);
        }

        supplierOrder.setSupplier(SupplierDto.fromEntity(supplierOptional.get()));
        return SupplierOrderDto.fromEntity(
            supplierOrderRepository.save(SupplierOrderDto.toEntity(supplierOrder))
        );
    }

    @Override
    public SupplierOrderDto updateProduct(Integer orderId, Integer orderLineId, Integer productId) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);
        checkProductId(productId, "nouvel");

        SupplierOrderDto supplierOrder = checkOrderStatus(orderId);
        Optional<SupplierOrderLine> supplierOrderLine = findSupplierOrderLine(orderLineId);

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucune article n'a ete trouve avec l'ID " + productId, ErrorCodes.PRODUCT_NOT_FOUND);
        }

        List<String> errors = ProductValidator.validate(ProductDto.fromEntity(productOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("Article invalid", ErrorCodes.PRODUCT_NOT_VALID, errors);
        }

        SupplierOrderLine supplierOrderLineSaved = supplierOrderLine.get();
        supplierOrderLineSaved.setProduct(productOptional.get());
        supplierOrderLineRepository.save(supplierOrderLineSaved);
        return supplierOrder;
    }

    @Override
    public SupplierOrderDto deleteProduct(Integer orderId, Integer orderLineId) {
        checkOrderId(orderId);
        checkOrderLineId(orderLineId);

        SupplierOrderDto supplierOrder = checkOrderStatus(orderId);
        // Just to check the SupplierOrderLine and inform the supplier in case it is absent
        findSupplierOrderLine(orderLineId);
        supplierOrderLineRepository.deleteById(orderLineId);
        return supplierOrder;
    }

    @Override
    public SupplierOrderDto findById(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return supplierOrderRepository.findById(id)
            .map(SupplierOrderDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune commande fournisseur n'a ete trouve avec l'ID " + id, ErrorCodes.SUPPLIER_ORDER_NOT_FOUND));
    }

    @Override
    public SupplierOrderDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande fournisseur CODE is NULL");
            return null;
        }
        return supplierOrderRepository.findSupplierOrderByCode(code)
            .map(SupplierOrderDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune commande fournisseur n'a ete trouve avec le CODE " + code, ErrorCodes.SUPPLIER_ORDER_NOT_FOUND));
    }

    @Override
    public List<SupplierOrderDto> findAll() {
        return supplierOrderRepository.findAll().stream()
            .map(SupplierOrderDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<SupplierOrderLineDto> findAllSupplierOrderLinesBySupplierOrderId(Integer orderId) {
        return supplierOrderLineRepository.findAllBySupplierOrderId(orderId).stream()
            .map(SupplierOrderLineDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id)  {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return;
        }
        List<SupplierOrderLine> supplierOrderLine = supplierOrderLineRepository.findAllBySupplierOrderId(id);
        if (!supplierOrderLine.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilisee", ErrorCodes.SUPPLIER_ORDER_ALREADY_IN_USE);
        }
        supplierOrderRepository.deleteById(id);
    }
}
