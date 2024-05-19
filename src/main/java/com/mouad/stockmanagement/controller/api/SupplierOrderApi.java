package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.SUPPLIER_ORDER_ENDPOINT;
import static com.mouad.stockmanagement.utils.Constants.CREATE_SUPPLIER_ORDER_ENDPOINT;
import static com.mouad.stockmanagement.utils.Constants.DELETE_SUPPLIER_ORDER_ENDPOINT;
import static com.mouad.stockmanagement.utils.Constants.FIND_ALL_SUPPLIER_ORDERS_ENDPOINT;
import static com.mouad.stockmanagement.utils.Constants.FIND_SUPPLIER_ORDER_BY_ID_ENDPOINT;
import static com.mouad.stockmanagement.utils.Constants.FIND_SUPPLIER_ORDER_BY_CODE_ENDPOINT;

import com.mouad.stockmanagement.dto.SupplierOrderDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;

// import io.swagger.annotations.Api;

import java.math.BigDecimal;
import java.util.List;

import com.mouad.stockmanagement.model.SupplierOrderLine;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Api("supplierOrder")
public interface SupplierOrderApi {

    @PostMapping(CREATE_SUPPLIER_ORDER_ENDPOINT)
    SupplierOrderDto save(@RequestBody SupplierOrderDto dto);

    @PatchMapping(SUPPLIER_ORDER_ENDPOINT + "/update/status/{orderId}/{orderStatus}")
    SupplierOrderDto updateOrderStatus(@PathVariable("orderId") Integer orderId, @PathVariable("orderStatus") OrderStatus orderStatus);

    @PatchMapping(SUPPLIER_ORDER_ENDPOINT + "/update/quantity/{orderId}/{orderLineId}/{quantity}")
    SupplierOrderDto updateOrderQuantity(@PathVariable("orderId") Integer orderId, @PathVariable("orderLineId") Integer orderLineId, @PathVariable("quantity") BigDecimal quantity);

    @PatchMapping(SUPPLIER_ORDER_ENDPOINT + "/update/supplier/{orderId}/{supplierId}")
    SupplierOrderDto updateSupplier(@PathVariable("orderId") Integer orderId, @PathVariable("supplierId") Integer supplierId);

    @PatchMapping(SUPPLIER_ORDER_ENDPOINT + "/update/product/{orderId}/{orderLineId}/{productId}")
    SupplierOrderDto updateProduct(@PathVariable("orderId") Integer orderId, @PathVariable("orderLineId") Integer orderLineId, @PathVariable("productId") Integer productId);

    @DeleteMapping(SUPPLIER_ORDER_ENDPOINT + "/delete/product/{orderId}/{orderLineId}")
    SupplierOrderDto deleteProduct(@PathVariable("orderId") Integer orderId, @PathVariable("orderLineId") Integer orderLineId);

    @GetMapping(FIND_SUPPLIER_ORDER_BY_ID_ENDPOINT)
    SupplierOrderDto findById(@PathVariable("supplierOrderId") Integer id);

    @GetMapping(FIND_SUPPLIER_ORDER_BY_CODE_ENDPOINT)
    SupplierOrderDto findByCode(@PathVariable("supplierOrderCode") String code);

    @GetMapping(FIND_ALL_SUPPLIER_ORDERS_ENDPOINT)
    List<SupplierOrderDto> findAll();

    @GetMapping(SUPPLIER_ORDER_ENDPOINT + "/supplierOrder/{orderId}")
    List<SupplierOrderLineDto> findAllSupplierOrderLinesBySupplierOrderId(@PathVariable("orderId") Integer orderId);

    @DeleteMapping(DELETE_SUPPLIER_ORDER_ENDPOINT)
    void delete(@PathVariable("orderSupplierId") Integer id);

}