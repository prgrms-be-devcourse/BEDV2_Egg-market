package com.devcourse.eggmarket.domain.model.image.exception;

public class FileDeleteFailException extends RuntimeException {

    public FileDeleteFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteFailException(String message) {
        super(message);
    }
}
