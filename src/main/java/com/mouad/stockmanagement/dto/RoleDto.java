package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private Integer id;
    private String roleName;

    @JsonIgnore
    private UserDto user;

    public static RoleDto fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDto.builder()
            .id(role.getId())
            .roleName(role.getRoleName())
            .build();
    }

    public static Role toEntity(RoleDto dto) {
        if (dto == null) {
            return null;
        }
        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());
        role.setUser(UserDto.toEntity(dto.getUser()));
        return role;
    }
}
