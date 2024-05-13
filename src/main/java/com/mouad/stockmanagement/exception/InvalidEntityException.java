package com.mouad.stockmanagement.exception;

import java.util.List;
import lombok.Getter;

public class InvalidEntityException extends RuntimeException {

    @Getter
    private ErrorCodes errorCode;
    @Getter
    private List<String> errors;

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable reason) {
        super(message, reason);
    }

    public InvalidEntityException(String message, Throwable reason, ErrorCodes errorCode) {
        super(message, reason);
        this.errorCode = errorCode;
    }

    public InvalidEntityException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidEntityException(String message, ErrorCodes errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

}
