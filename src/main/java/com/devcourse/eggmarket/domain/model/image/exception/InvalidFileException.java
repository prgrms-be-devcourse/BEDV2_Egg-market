package com.devcourse.eggmarket.domain.model.image.exception;

import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class InvalidFileException extends InvalidValueException {

    public InvalidFileException(String message) {
        super(message);
    }
}
