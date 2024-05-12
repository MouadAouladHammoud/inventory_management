package com.mouad.stockmanagement.model;

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
@Table(name = "categories")
public class Category extends AbstractEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "designation")
    private String designation;

    @Column(name = "company_id")
    private Integer companyId;

    // mappedBy = "category" => il doit correspondre exactement au nom de la propriété dans la classe "Product" qui fait référence à la classe "Category"
    // la propriété dans la classe "Product" est nommée "category", donc il faut utiliser "category" comme valeur de mappedBy dans la classe "Category".
    @OneToMany(mappedBy = "category") //
    private List<Product> products;

}
