package com.mouad.stockmanagement.model;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
public class Product extends AbstractEntity {

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "designation")
    private String designation;

    @Column(name = "unit_price_ht")
    private BigDecimal unitPriceHt;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "unit_price_ttc")
    private BigDecimal unitPriceTtc;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<SaleLine> saleLines;

    @OneToMany(mappedBy = "product")
    private List<ClientOrderLine> clientOrderLines;

    @OneToMany(mappedBy = "product")
    private List<SupplierOrderLine> supplierOrderLines;

    @OneToMany(mappedBy = "product")
    private List<StockMovement> stockMovements;
}

// OK