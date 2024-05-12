package com.mouad.stockmanagement.validator;

import com.mouad.stockmanagement.dto.UserDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public class UserValidator {

    public static List<String> validate(UserDto userDto) {
        List<String> errors = new ArrayList<>();
        if (userDto == null) {
            errors.add("Veuillez renseigner le nom d'utilisateur");
            errors.add("Veuillez renseigner le prenom d'utilisateur");
            errors.add("Veuillez renseigner le mot de passe d'utilisateur");
            errors.add("Veuillez renseigner l'adresse d'utilisateur");
            errors.addAll(AddressValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(userDto.getLastName())) {
            errors.add("Veuillez renseigner le nom d'utilisateur");
        }
        if (!StringUtils.hasLength(userDto.getFirstName())) {
            errors.add("Veuillez renseigner le prenom d'utilisateur");
        }
        if (!StringUtils.hasLength(userDto.getEmail())) {
            errors.add("Veuillez renseigner l'email d'utilisateur");
        }
        if (!StringUtils.hasLength(userDto.getPassword())) {
            errors.add("Veuillez renseigner le mot de passe d'utilisateur");
        }
        if (userDto.getDateOfBirth() == null) {
            errors.add("Veuillez renseigner la date de naissance d'utilisateur");
        }
        errors.addAll(AddressValidator.validate(userDto.getAddress()));
        return errors;
    }

}
