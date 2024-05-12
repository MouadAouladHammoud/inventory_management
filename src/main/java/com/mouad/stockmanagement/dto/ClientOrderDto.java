package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.ClientOrder;
import com.mouad.stockmanagement.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientOrderDto {
    private Integer id;
    private String code;
    private Instant orderDate;
    private OrderStatus orderStatus;
    private ClientDto client;
    private Integer companyId;
    private List<ClientOrderLineDto> clientOrderLines;

    public static ClientOrderDto fromEntity(ClientOrder clientOrder) {
        if (clientOrder == null) {
            return null;
        }
        return ClientOrderDto.builder()
            .id(clientOrder.getId())
            .code(clientOrder.getCode())
            .orderDate(clientOrder.getOrderDate())
            .orderStatus(clientOrder.getOrderStatus())
            .client(ClientDto.fromEntity(clientOrder.getClient()))
            .companyId(clientOrder.getCompanyId())
            .build();
    }

    public static ClientOrder toEntity(ClientOrderDto dto) {
        if (dto == null) {
            return null;
        }
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setId(dto.getId());
        clientOrder.setCode(dto.getCode());
        clientOrder.setClient(ClientDto.toEntity(dto.getClient()));
        clientOrder.setOrderDate(dto.getOrderDate());
        clientOrder.setOrderStatus(dto.getOrderStatus());
        clientOrder.setCompanyId(dto.getCompanyId());
        return clientOrder;
    }

    public boolean isOrderDelivered() {
        return OrderStatus.DELIVERED.equals(this.orderStatus);
    }
}
