package com.mouad.stockmanagement.model;

import java.time.Instant;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "sales")
public class Sale extends AbstractEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "sale_date")
    private Instant saleDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "company_id")
    private Integer companyId;

    @OneToMany(mappedBy = "sale")
    private List<SaleLine> saleLines;
}