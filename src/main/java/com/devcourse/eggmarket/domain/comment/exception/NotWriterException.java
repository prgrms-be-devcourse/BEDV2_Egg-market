package com.devcourse.eggmarket.domain.comment.exception;

import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class NotWriterException extends InvalidValueException {

    public NotWriterException(String value,
        ErrorCode errorCode) {
        super(value, errorCode);
    }
}
