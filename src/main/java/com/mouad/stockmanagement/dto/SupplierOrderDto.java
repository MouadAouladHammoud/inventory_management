package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.SupplierOrder;
import com.mouad.stockmanagement.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.Instant;
import java.util.List;

import com.mouad.stockmanagement.model.SupplierOrderLine;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierOrderDto {
    private Integer id;
    private String code;
    private Instant orderDate;
    private OrderStatus orderStatus;
    private SupplierDto supplier;
    private Integer companyId;
    private List<SupplierOrderLineDto> supplierOrderLines;

    public static SupplierOrderDto fromEntity(SupplierOrder supplierOrder) {
        if (supplierOrder == null) {
            return null;
        }
        return SupplierOrderDto.builder()
            .id(supplierOrder.getId())
            .code(supplierOrder.getCode())
            .orderDate(supplierOrder.getOrderDate())
            .supplier(SupplierDto.fromEntity(supplierOrder.getSupplier()))
            .orderStatus(supplierOrder.getOrderStatus())
            .companyId(supplierOrder.getCompanyId())
            .build();
    }

    public static SupplierOrder toEntity(SupplierOrderDto dto) {
        if (dto == null) {
            return null;
        }
        SupplierOrder supplierOrder = new SupplierOrder();
        supplierOrder.setId(dto.getId());
        supplierOrder.setCode(dto.getCode());
        supplierOrder.setOrderDate(dto.getOrderDate());
        supplierOrder.setSupplier(SupplierDto.toEntity(dto.getSupplier()));
        supplierOrder.setCompanyId(dto.getCompanyId());
        supplierOrder.setOrderStatus(dto.getOrderStatus());
        return supplierOrder;
    }

    public boolean isOrderDelivered() {
        return OrderStatus.DELIVERED.equals(this.orderStatus);
    }
}
