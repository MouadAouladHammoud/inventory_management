package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.SupplierOrder;
import com.mouad.stockmanagement.model.SupplierOrderLine;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierOrderLineDto {
    private Integer id;
    private ProductDto product;
    private SupplierOrder supplierOrder;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private Integer companyId;

    public static SupplierOrderLineDto fromEntity(SupplierOrderLine supplierOrderLine) {
        if (supplierOrderLine == null) {
            return null;
        }
        return SupplierOrderLineDto.builder()
            .id(supplierOrderLine.getId())
            .product(ProductDto.fromEntity(supplierOrderLine.getProduct()))
            .quantity(supplierOrderLine.getQuantity())
            .unitPrice(supplierOrderLine.getUnitPrice())
            .companyId(supplierOrderLine.getCompanyId())
            .build();
    }

    public static SupplierOrderLine toEntity(SupplierOrderLineDto dto) {
        if (dto == null) {
            return null;
        }
        SupplierOrderLine supplierOrderLine = new SupplierOrderLine();
        supplierOrderLine.setId(dto.getId());
        supplierOrderLine.setProduct(ProductDto.toEntity(dto.getProduct()));
        supplierOrderLine.setUnitPrice(dto.getUnitPrice());
        supplierOrderLine.setQuantity(dto.getQuantity());
        supplierOrderLine.setCompanyId(dto.getCompanyId());
        return supplierOrderLine;
    }
}
