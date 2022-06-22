package com.devcourse.eggmarket.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // global error
    INVALID_INPUT("G01", "Invalid Input Value", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final String code, final String message, final HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
