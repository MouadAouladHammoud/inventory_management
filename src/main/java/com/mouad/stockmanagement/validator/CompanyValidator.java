package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.CompanyDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class CompanyValidator {
    public static List<String> validate(CompanyDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Veuillez renseigner le nom de l'entreprise");
            errors.add("Veuillez reseigner la description de l'entreprise");
            errors.add("Veuillez reseigner le code fiscal de l'entreprise");
            errors.add("Veuillez reseigner l'email de l'entreprise");
            errors.add("Veuillez reseigner le numero de telephone de l'entreprise");
            errors.addAll(AddressValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(dto.getName())) {
            errors.add("Veuillez renseigner le nom de l'entreprise");
        }
        if (!StringUtils.hasLength(dto.getDescription())) {
            errors.add("Veuillez reseigner la description de l'entreprise");
        }
        if (!StringUtils.hasLength(dto.getTaxCode())) {
            errors.add("Veuillez reseigner le code fiscal de l'entreprise");
        }
        if (!StringUtils.hasLength(dto.getEmail())) {
            errors.add("Veuillez reseigner l'email de l'entreprise");
        }
        if (!StringUtils.hasLength(dto.getPhoneNumber())) {
            errors.add("Veuillez reseigner le numero de telephone de l'entreprise");
        }

        errors.addAll(AddressValidator.validate(dto.getAddress()));
        return errors;
    }
}
