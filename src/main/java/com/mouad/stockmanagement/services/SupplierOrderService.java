package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.SupplierOrderDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public interface SupplierOrderService {

    SupplierOrderDto save(SupplierOrderDto dto);

    SupplierOrderDto updateOrderStatus(Integer orderId, OrderStatus orderStatus);

    SupplierOrderDto updateOrderQuantity(Integer orderId, Integer orderLineId, BigDecimal quantity);

    SupplierOrderDto updateSupplier(Integer orderId, Integer supplierId);

    SupplierOrderDto updateProduct(Integer orderId, Integer orderLineId, Integer productId);

    // Delete article ==> delete SupplierOrderLine
    SupplierOrderDto deleteProduct(Integer orderId, Integer orderLineId);

    SupplierOrderDto findById(Integer id);

    SupplierOrderDto findByCode(String code);

    List<SupplierOrderDto> findAll();

    List<SupplierOrderLineDto> findAllSupplierOrderLinesBySupplierOrderId(Integer orderId);

    void delete(Integer id);

}
