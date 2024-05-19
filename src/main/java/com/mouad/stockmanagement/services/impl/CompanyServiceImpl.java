package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.CompanyDto;
import com.mouad.stockmanagement.dto.RoleDto;
import com.mouad.stockmanagement.dto.UserDto;
import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.repository.ClientRepository;
import com.mouad.stockmanagement.repository.CompanyRepository;
import com.mouad.stockmanagement.repository.RoleRepository;
import com.mouad.stockmanagement.repository.UserRepository;
import com.mouad.stockmanagement.services.CompanyService;
import com.mouad.stockmanagement.services.UserService;
import com.mouad.stockmanagement.validator.CompanyValidator;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional(rollbackOn = Exception.class)
@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserService userService;
    private RoleRepository roleRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, UserService userService, RoleRepository roleRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public CompanyDto save(CompanyDto dto) {
        List<String> errors = CompanyValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Company is not valid {}", dto);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.COMPANY_NOT_VALID, errors);
        }
        CompanyDto companySaved = CompanyDto.fromEntity(
            companyRepository.save(CompanyDto.toEntity(dto))
        );

        UserDto user = fromCompany(companySaved);
        UserDto savedUser = userService.save(user);

        RoleDto roleDto = RoleDto.builder()
                .roleName("ADMIN")
                .user(savedUser)
                .build();
        roleRepository.save(RoleDto.toEntity(roleDto));

        return  companySaved;
    }

    private UserDto fromCompany(CompanyDto dto) {
        return UserDto.builder()
            .address(dto.getAddress())
            .lastName(dto.getName())
            .firstName(dto.getTaxCode())
            .email(dto.getEmail())
            .password(generateRandomPassword())
            .company(dto)
            .dateOfBirth(Instant.now())
            .photoUrl(dto.getImageUrl())
            .build();
    }

    private String generateRandomPassword() {
        return "som3R@nd0mP@$$word";
    }

    @Override
    public CompanyDto findById(Integer id) {
        if (id == null) {
            log.error("Company ID is null");
            return null;
        }
        return companyRepository.findById(id)
            .map(CompanyDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException(
                "Aucune entreprise avec l'ID = " + id + " n' ete trouve dans la BDD", ErrorCodes.COMPANY_NOT_FOUND)
            );
    }

    @Override
    public List<CompanyDto> findAll() {
        return companyRepository.findAll().stream()
            .map(CompanyDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Company ID is null");
            return;
        }
        companyRepository.deleteById(id);
    }
}
