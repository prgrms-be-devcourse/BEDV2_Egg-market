package com.devcourse.eggmarket.domain.post.exception;

import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class InvalidBuyerException extends InvalidValueException {

    public InvalidBuyerException(String value,
        ErrorCode errorCode) {
        super(value, errorCode);
    }
}
