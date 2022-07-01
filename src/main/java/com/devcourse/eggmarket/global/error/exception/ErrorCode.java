package com.devcourse.eggmarket.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // global error
    INVALID_INPUT("G01", "유효하지 않은 입력값 입니다.", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("G02", "찾을 수 없는 엔티티입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("G03", "서버 에러 입니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // user error
    USER_NOT_FOUND("U01", "찾을 수 없는 유저입니다.", HttpStatus.BAD_REQUEST),

    // eval error
    SCORE_INPUT_INVALID("E01", "유효하지 않은 점수 입니다.", HttpStatus.BAD_REQUEST),

    // comment error
    SOLD_OUT_POST_NOT_ALLOWED_COMMENT_ERROR("C01", "판매 완료된 글에는 댓글을 작성할 수 없습니다",
        HttpStatus.BAD_REQUEST),
    NOT_ALLOWED_BUYER("C02", "해당 판매글에 대한 구매자로 등록할 수 없습니다", HttpStatus.BAD_REQUEST),
    NOT_ALLOWED_USER("C03", "해당 댓글에 대한 권한이 없는 사용자입니다", HttpStatus.FORBIDDEN),

    // post error
    NOT_VALID_CATEGORY("P01", "존재하지 않는 카테고리 입니다.", HttpStatus.BAD_REQUEST);

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
