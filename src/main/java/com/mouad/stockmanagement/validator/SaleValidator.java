package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.SaleDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class SaleValidator {

    public static List<String> validate(SaleDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuillez renseigner le code de vente");
            errors.add("Veuillez renseigner la date de vente");
            return errors;
        }

        if (!StringUtils.hasLength(dto.getCode())) {
            errors.add("Veuillez renseigner le code de vente");
        }
        if (dto.getSaleDate() == null) {
            errors.add("Veuillez renseigner la date de vente");
        }
        return errors;
    }

}