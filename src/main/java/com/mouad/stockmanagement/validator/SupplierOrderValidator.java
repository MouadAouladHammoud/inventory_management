package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.SupplierOrderDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class SupplierOrderValidator {

    public static List<String> validate(SupplierOrderDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuillez renseigner le code de la commande");
            errors.add("Veuillez renseigner la date de la commande");
            errors.add("Veuillez renseigner l'etat de la commande");
            errors.add("Veuillez renseigner le client");
            return errors;
        }

        if (!StringUtils.hasLength(dto.getCode())) {
            errors.add("Veuillez renseigner le code de la commande");
        }
        if (dto.getOrderDate() == null) {
            errors.add("Veuillez renseigner la date de la commande");
        }
        if (!StringUtils.hasLength(dto.getOrderStatus().toString())) {
            errors.add("Veuillez renseigner l'etat de la commande");
        }
        if (dto.getSupplier() == null || dto.getSupplier().getId() == null) {
            errors.add("Veuillez renseigner le fournisseur");
        }
        return errors;
    }

}
