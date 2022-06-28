package com.devcourse.eggmarket.domain.comment.exception;

import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class CommentNotAllowedPostException extends InvalidValueException {

    public CommentNotAllowedPostException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
