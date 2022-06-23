package com.devcourse.eggmarket.domain.post.exception;

import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class AlreadyCompletedException extends InvalidValueException {

    public AlreadyCompletedException(String message) {
        super(message);
    }
}
