package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.User;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String lastName;
    private String firstName;
    private String email;
    private Instant dateOfBirth;
    private String password;
    private AddressDto address;
    private String photoUrl;
    private CompanyDto company;
    private List<RoleDto> roles;

    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
            .id(user.getId())
            .lastName(user.getLastName())
            .firstName(user.getFirstName())
            .email(user.getEmail())
            .password(user.getPassword())
            .dateOfBirth(user.getDateOfBirth())
            .address(AddressDto.fromEntity(user.getAddress()))
            .photoUrl(user.getPhotoUrl())
            .company(CompanyDto.fromEntity(user.getCompany()))
            .roles(
                user.getRoles() != null
                ? user.getRoles().stream().map(RoleDto::fromEntity).collect(Collectors.toList())
                : null
            )
            .build();
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(AddressDto.toEntity(dto.getAddress()));
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setCompany(CompanyDto.toEntity(dto.getCompany()));
        return user;
    }
}
