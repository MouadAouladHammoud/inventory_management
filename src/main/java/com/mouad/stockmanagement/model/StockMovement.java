package com.mouad.stockmanagement.model;

import java.math.BigDecimal;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "stock_movements")
public class StockMovement extends AbstractEntity {

    @Column(name = "movement_date")
    private Instant movementDate;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "movement_type")
    @Enumerated(EnumType.STRING)
    private StockMovementType stockMovementType;

    @Column(name = "source_movement")
    @Enumerated(EnumType.STRING)
    private StockMovementSource stockMovementSource;

    @Column(name = "company_id")
    private Integer companyId;
}
