package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.ClientOrderDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public interface ClientOrderService {
    ClientOrderDto save(ClientOrderDto dto);

    ClientOrderDto updateOrderStatus(Integer orderId, OrderStatus orderStatus);

    ClientOrderDto updateQuantityOrder(Integer orderId, Integer orderLineId, BigDecimal quantity);

    ClientOrderDto updateClient(Integer orderId, Integer idClient);

    ClientOrderDto updateProduct(Integer orderId, Integer orderLineId, Integer productId);

    // Delete article ==> delete ClientOrderLines
    ClientOrderDto deleteProduct(Integer orderId, Integer orderLineId);

    ClientOrderDto findById(Integer id);

    ClientOrderDto findByCode(String code);

    List<ClientOrderDto> findAll();

    List<ClientOrderLineDto> findAllClientOrderLinesByClientOrderId(Integer orderId);

    void delete(Integer id);
}
