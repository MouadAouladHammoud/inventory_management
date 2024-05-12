package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Sale;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleDto {
    private Integer id;
    private String code;
    private Instant saleDate;
    private String comment;
    private Integer companyId;
    private List<SaleLineDto> saleLines;

    public static SaleDto fromEntity(Sale sale) {
        if (sale == null) {
            return null;
        }
        return SaleDto.builder()
            .id(sale.getId())
            .code(sale.getCode())
            .comment(sale.getComment())
            .companyId(sale.getCompanyId())
            .build();
    }

    public static Sale toEntity(SaleDto dto) {
        if (dto == null) {
            return null;
        }
        Sale sale = new Sale();
        sale.setId(dto.getId());
        sale.setCode(dto.getCode());
        sale.setComment(dto.getComment());
        sale.setCompanyId(dto.getCompanyId());
        return sale;
    }
}
