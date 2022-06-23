package com.devcourse.eggmarket.domain.post.exception;

import com.devcourse.eggmarket.global.error.exception.EntityNotFoundException;

public class NotExistBuyerException extends EntityNotFoundException {

    public NotExistBuyerException(String message) {
        super(message);
    }
}
