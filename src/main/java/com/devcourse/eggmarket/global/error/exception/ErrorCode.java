package com.devcourse.eggmarket.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // global error
    INVALID_INPUT("G01", "유효하지 않은 입력값 입니다.", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("G02", "찾을 수 없는 엔티티입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("G03", "서버 에러 입니다", HttpStatus.INTERNAL_SERVER_ERROR);

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
