package com.mouad.stockmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangeUserPasswordDto {
    private Integer id;
    private String password;
    private String confirmPassword;
}
