package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.SaleLine;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleLineDto {
    private Integer id;
    private SaleDto sale;
    private ProductDto product;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private Integer companyId;

    public static SaleLineDto fromEntity(SaleLine saleLine) {
        if (saleLine == null) {
            return null;
        }
        return SaleLineDto.builder()
            .id(saleLine.getId())
            .sale(SaleDto.fromEntity(saleLine.getSale()))
            .product(ProductDto.fromEntity(saleLine.getProduct()))
            .quantity(saleLine.getQuantity())
            .unitPrice(saleLine.getUnitPrice())
            .companyId(saleLine.getCompanyId())
            .build();
    }

    public static SaleLine toEntity(SaleLineDto dto) {
        if (dto == null) {
            return null;
        }
        SaleLine saleLine = new SaleLine();
        saleLine.setId(dto.getId());
        saleLine.setSale(SaleDto.toEntity(dto.getSale()));
        saleLine.setProduct(ProductDto.toEntity(dto.getProduct()));
        saleLine.setQuantity(dto.getQuantity());
        saleLine.setUnitPrice(dto.getUnitPrice());
        saleLine.setCompanyId(dto.getCompanyId());
        return saleLine;
    }
}
