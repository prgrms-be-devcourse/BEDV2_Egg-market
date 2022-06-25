package com.devcourse.eggmarket.domain.post.exception;

import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class NotMatchedSellerException extends InvalidValueException {

    public NotMatchedSellerException(String value, Long sellerId, Long loginId) {
        super(value + sellerId + loginId);
    }
}
