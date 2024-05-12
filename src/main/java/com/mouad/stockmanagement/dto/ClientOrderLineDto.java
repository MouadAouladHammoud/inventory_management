package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.ClientOrderLine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientOrderLineDto {
    private Integer id;
    private ProductDto product;

    @JsonIgnore
    private ClientOrderDto clientOrder;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private Integer companyId;

    public static ClientOrderLineDto fromEntity(ClientOrderLine clientOrderLine) {
        if (clientOrderLine == null) {
            return null;
        }
        return ClientOrderLineDto.builder()
            .id(clientOrderLine.getId())
            .product(ProductDto.fromEntity(clientOrderLine.getProduct()))
            .quantity(clientOrderLine.getQuantity())
            .unitPrice(clientOrderLine.getUnitPrice())
            .companyId(clientOrderLine.getCompanyId())
            .build();
    }

    public static ClientOrderLine toEntity(ClientOrderLineDto dto) {
        if (dto == null) {
            return null;
        }
        ClientOrderLine clientOrderLine = new ClientOrderLine();
        clientOrderLine.setId(dto.getId());
        clientOrderLine.setProduct(ProductDto.toEntity(dto.getProduct()));
        clientOrderLine.setUnitPrice(dto.getUnitPrice());
        clientOrderLine.setQuantity(dto.getQuantity());
        clientOrderLine.setCompanyId(dto.getCompanyId());
        return clientOrderLine;
    }
}
