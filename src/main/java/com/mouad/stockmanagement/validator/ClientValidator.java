package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.ClientDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class ClientValidator {
    public static List<String> validate(ClientDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Veuillez renseigner le nom du client");
            errors.add("Veuillez renseigner le prenom du client");
            errors.add("Veuillez renseigner le Mail du client");
            errors.add("Veuillez renseigner le numero de telephone du client");
            errors.addAll(AddressValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(dto.getLastName())) {
            errors.add("Veuillez renseigner le nom du client");
        }
        if (!StringUtils.hasLength(dto.getFirstName())) {
            errors.add("Veuillez renseigner le prenom du client");
        }
        if (!StringUtils.hasLength(dto.getEmail())) {
            errors.add("Veuillez renseigner le Mail du client");
        }
        if (!StringUtils.hasLength(dto.getPhoneNumber())) {
            errors.add("Veuillez renseigner le numero de telephone du client");
        }
        errors.addAll(AddressValidator.validate(dto.getAddress()));
        return errors;
    }
}