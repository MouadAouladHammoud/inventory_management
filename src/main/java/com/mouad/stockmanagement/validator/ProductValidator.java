package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.ProductDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class ProductValidator {
    public static List<String> validate(ProductDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le code de l'article");
            errors.add("Veuillez renseigner la designation de l'article");
            errors.add("Veuillez renseigner le prix unitaire HT l'article");
            errors.add("Veuillez renseigner le taux TVA de l'article");
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
            errors.add("Veuillez selectionner une categorie");
            return errors;
        }

        if (!StringUtils.hasLength(dto.getProductCode())) {
            errors.add("Veuillez renseigner le code de l'article");
        }
        if (!StringUtils.hasLength(dto.getDesignation())) {
            errors.add("Veuillez renseigner la designation de l'article");
        }
        if (dto.getUnitPriceHt() == null) {
            errors.add("Veuillez renseigner le prix unitaire HT l'article");
        }
        if (dto.getTaxRate() == null) {
            errors.add("Veuillez renseigner le taux TVA de l'article");
        }
        if (dto.getUnitPriceTtc() == null) {
            errors.add("Veuillez renseigner le prix unitaire TTC de l'article");
        }
        if (dto.getCategory() == null || dto.getCategory().getId() == null) {
            errors.add("Veuillez selectionner une categorie");
        }
        return errors;
    }
}
