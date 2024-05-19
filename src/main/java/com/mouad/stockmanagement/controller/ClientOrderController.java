package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.ClientOrderApi;
import com.mouad.stockmanagement.dto.ClientOrderDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;
import com.mouad.stockmanagement.services.ClientOrderService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

public class ClientOrderController implements ClientOrderApi {
    private ClientOrderService clientOrderService;
    @Autowired
    public ClientOrderController(ClientOrderService clientOrderService) {
        this.clientOrderService = clientOrderService;
    }


    @Override
    public ResponseEntity<ClientOrderDto> save(ClientOrderDto dto) {
        return ResponseEntity.ok(clientOrderService.save(dto));
    }

    @Override
    public ResponseEntity<ClientOrderDto> updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        return ResponseEntity.ok(clientOrderService.updateOrderStatus(orderId, orderStatus));
    }

    @Override
    public ResponseEntity<ClientOrderDto> updateQuantityOrder(Integer orderId, Integer orderLineId, BigDecimal quantity) {
        return ResponseEntity.ok(clientOrderService.updateQuantityOrder(orderId, orderLineId, quantity));
    }

    @Override
    public ResponseEntity<ClientOrderDto> updateClient(Integer orderId, Integer clientId) {
        return ResponseEntity.ok(clientOrderService.updateClient(orderId, clientId));
    }

    @Override
    public ResponseEntity<ClientOrderDto> updateProduct(Integer orderId, Integer orderLineId, Integer productId) {
        return ResponseEntity.ok(clientOrderService.updateProduct(orderId, orderLineId, productId));
    }

    @Override
    public ResponseEntity<ClientOrderDto> deleteProduct(Integer orderId, Integer orderLineId) {
        return ResponseEntity.ok(clientOrderService.deleteProduct(orderId, orderLineId));
    }

    @Override
    public ResponseEntity<ClientOrderDto> findById(Integer clientOrderId) {
        return ResponseEntity.ok(clientOrderService.findById(clientOrderId));
    }

    @Override
    public ResponseEntity<ClientOrderDto> findByCode(String code) {
        return ResponseEntity.ok(clientOrderService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<ClientOrderDto>> findAll() {
        return ResponseEntity.ok(clientOrderService.findAll());
    }

    @Override
    public ResponseEntity<List<ClientOrderLineDto>> findAllClientOrderLinesByClientOrderId(Integer orderId) {
        return ResponseEntity.ok(clientOrderService.findAllClientOrderLinesByClientOrderId(orderId));
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        clientOrderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
