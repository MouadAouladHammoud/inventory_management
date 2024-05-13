package com.mouad.stockmanagement.handlers;

import com.mouad.stockmanagement.exception.ErrorCodes;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {
    private ErrorCodes code;
    private Integer httpCode;
    private String message;
    private List<String> errors = new ArrayList<>();
}
