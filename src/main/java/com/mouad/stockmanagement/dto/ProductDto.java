package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Product;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDto {
    private Integer id;
    private String productCode;
    private String designation;
    private BigDecimal unitPriceHt;
    private BigDecimal taxRate;
    private BigDecimal unitPriceTtc;
    private String imageUrl;
    private CategoryDto category;
    private Integer companyId;

    public static ProductDto fromEntity(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
            .id(product.getId())
            .productCode(product.getProductCode())
            .designation(product.getDesignation())
            .imageUrl(product.getImageUrl())
            .unitPriceHt(product.getUnitPriceHt())
            .unitPriceTtc(product.getUnitPriceTtc())
            .taxRate(product.getTaxRate())
            .companyId(product.getCompanyId())
            .category(CategoryDto.fromEntity(product.getCategory()))
            .build();
    }

    public static Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setProductCode(productDto.getProductCode());
        product.setDesignation(productDto.getDesignation());
        product.setImageUrl(productDto.getImageUrl());
        product.setUnitPriceHt(productDto.getUnitPriceHt());
        product.setUnitPriceTtc(productDto.getUnitPriceTtc());
        product.setTaxRate(productDto.getTaxRate());
        product.setCompanyId(productDto.getCompanyId());
        product.setCategory(CategoryDto.toEntity(productDto.getCategory()));
        return product;
    }
}
