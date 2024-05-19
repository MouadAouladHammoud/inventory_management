package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import com.mouad.stockmanagement.dto.ClientOrderDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.model.OrderStatus;
// import io.swagger.annotations.Api;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Api("clientOrders")
public interface ClientOrderApi {
    @PostMapping(APP_ROOT + "/clientOrder/create")
    ResponseEntity<ClientOrderDto> save(@RequestBody ClientOrderDto dto);

    @PatchMapping(APP_ROOT + "/clientOrder/update/status/{orderId}/{orderStatus}")
    ResponseEntity<ClientOrderDto> updateOrderStatus(@PathVariable("orderId") Integer orderId, @PathVariable("orderStatus") OrderStatus orderStatus);

    @PatchMapping(APP_ROOT + "/clientOrder/update/quantity/{orderId}/{orderLineId}/{quantity}")
    ResponseEntity<ClientOrderDto> updateQuantityOrder(@PathVariable("orderId") Integer orderId,  @PathVariable("orderLineId") Integer orderLineId, @PathVariable("quantity") BigDecimal quantity);

    @PatchMapping(APP_ROOT + "/clientOrder/update/client/{orderId}/{clientId}")
    ResponseEntity<ClientOrderDto> updateClient(@PathVariable("orderId") Integer orderId, @PathVariable("clientId") Integer clientId);

    @PatchMapping(APP_ROOT + "/clientOrder/update/product/{orderId}/{orderLineId}/{productId}")
    ResponseEntity<ClientOrderDto> updateProduct(@PathVariable("orderId") Integer orderId, @PathVariable("orderLineId") Integer orderLineId, @PathVariable("productId") Integer productId);

    @DeleteMapping(APP_ROOT + "/clientOrder/delete/product/{orderId}/{orderLineId}")
    ResponseEntity<ClientOrderDto> deleteProduct(@PathVariable("orderId") Integer orderId, @PathVariable("orderLineId") Integer orderLineId);

    @GetMapping(APP_ROOT + "/clientOrder/{clientOrderId}")
    ResponseEntity<ClientOrderDto> findById(@PathVariable Integer clientOrderId);

    @GetMapping(APP_ROOT + "/clientOrder/filter/{clientOrderCode}")
    ResponseEntity<ClientOrderDto> findByCode(@PathVariable("clientOrderCode") String code);

    @GetMapping(APP_ROOT + "/clientOrder/all")
    ResponseEntity<List<ClientOrderDto>> findAll();

    @GetMapping(APP_ROOT + "/clientOrder/orderLines/{orderId}")
    ResponseEntity<List<ClientOrderLineDto>> findAllClientOrderLinesByClientOrderId(@PathVariable("orderId") Integer orderId);

    @DeleteMapping(APP_ROOT + "/clientOrders/delete/{orderClientId}")
    ResponseEntity<Void> delete(@PathVariable("orderClientId") Integer id);
}
