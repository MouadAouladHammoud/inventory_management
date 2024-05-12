package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.AddressDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class AddressValidator {
    public static List<String> validate(AddressDto addressDto) {
        List<String> errors = new ArrayList<>();

        if (addressDto == null) {
            errors.add("Veuillez renseigner l'adresse 1'");
            errors.add("Veuillez renseigner la ville'");
            errors.add("Veuillez renseigner le pays'");
            errors.add("Veuillez renseigner le code postal'");
            return errors;
        }
        if (!StringUtils.hasLength(addressDto.getAddress1())) {
            errors.add("Veuillez renseigner l'adresse 1'");
        }
        if (!StringUtils.hasLength(addressDto.getCity())) {
            errors.add("Veuillez renseigner la ville'");
        }
        if (!StringUtils.hasLength(addressDto.getCountry())) {
            errors.add("Veuillez renseigner le pays'");
        }
        if (!StringUtils.hasLength(addressDto.getPostal_code())) {
            errors.add("Veuillez renseigner le code postal'");
        }
        return errors;
    }
}
