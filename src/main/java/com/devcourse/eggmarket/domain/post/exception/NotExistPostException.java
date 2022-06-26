package com.devcourse.eggmarket.domain.post.exception;

import com.devcourse.eggmarket.global.error.exception.EntityNotFoundException;

public class NotExistPostException extends EntityNotFoundException {

    public NotExistPostException(String message, Long id) {
        super(message + id);
    }
}
