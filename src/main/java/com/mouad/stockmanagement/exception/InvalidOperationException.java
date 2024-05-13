package com.mouad.stockmanagement.exception;

import lombok.Getter;

public class InvalidOperationException extends RuntimeException {

    @Getter
    private ErrorCodes errorCode;

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable reason) {
        super(message, reason);
    }

    public InvalidOperationException(String message, Throwable reason, ErrorCodes errorCode) {
        super(message, reason);
        this.errorCode = errorCode;
    }

    public InvalidOperationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}