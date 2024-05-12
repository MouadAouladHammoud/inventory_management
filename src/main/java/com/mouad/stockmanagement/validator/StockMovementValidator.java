package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.StockMovementDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class StockMovementValidator {

    public static List<String> validate(StockMovementDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuillez renseigner la date du mouvenent");
            errors.add("Veuillez renseigner la quantite du mouvenent");
            errors.add("Veuillez renseigner l'article");
            errors.add("Veuillez renseigner le type du mouvement");
            return errors;
        }

        if (dto.getMovementDate() == null) {
            errors.add("Veuillez renseigner la date du mouvenent");
        }
        if (dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Veuillez renseigner la quantite du mouvenent");
        }
        if (dto.getProduct() == null || dto.getProduct().getId() == null) {
            errors.add("Veuillez renseigner l'article");
        }
        if (!StringUtils.hasLength(dto.getStockMovementType().name())) {
            errors.add("Veuillez renseigner le type du mouvement");
        }
        return errors;
    }

}
