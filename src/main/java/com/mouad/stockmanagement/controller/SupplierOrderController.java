package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.SupplierOrderApi;
import com.mouad.stockmanagement.dto.SupplierOrderDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;
import com.mouad.stockmanagement.services.SupplierOrderService;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

public class SupplierOrderController implements SupplierOrderApi {

    private SupplierOrderService supplierOrderService;
    @Autowired
    public SupplierOrderController(SupplierOrderService supplierOrderService) {
        this.supplierOrderService = supplierOrderService;
    }

    @Override
    public SupplierOrderDto save(SupplierOrderDto dto) {
        return supplierOrderService.save(dto);
    }

    @Override
    public SupplierOrderDto updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        return supplierOrderService.updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public SupplierOrderDto updateOrderQuantity(Integer orderId, Integer orderLineId, BigDecimal quantity) {
        return supplierOrderService.updateOrderQuantity(orderId, orderLineId, quantity);
    }

    @Override
    public SupplierOrderDto updateSupplier(Integer orderId, Integer supplierId) {
        return supplierOrderService.updateSupplier(orderId, supplierId);
    }

    @Override
    public SupplierOrderDto updateProduct(Integer orderId, Integer orderLineId, Integer productId) {
        return supplierOrderService.updateProduct(orderId, orderLineId, productId);
    }

    @Override
    public SupplierOrderDto deleteProduct(Integer orderId, Integer orderLineId) {
        return supplierOrderService.deleteProduct(orderId, orderLineId);
    }

    @Override
    public SupplierOrderDto findById(Integer id) {
        return supplierOrderService.findById(id);
    }

    @Override
    public SupplierOrderDto findByCode(String code) {
        return supplierOrderService.findByCode(code);
    }

    @Override
    public List<SupplierOrderDto> findAll() {
        return supplierOrderService.findAll();
    }

    @Override
    public List<SupplierOrderLineDto> findAllSupplierOrderLinesBySupplierOrderId(Integer orderId) {
        return supplierOrderService.findAllSupplierOrderLinesBySupplierOrderId(orderId);
    }

    @Override
    public void delete(Integer id) {
        supplierOrderService.delete(id);
    }
}
