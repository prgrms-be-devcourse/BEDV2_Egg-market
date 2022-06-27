package com.devcourse.eggmarket.domain.user.exception;

import com.devcourse.eggmarket.global.error.exception.EntityNotFoundException;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;

public class NotExistUserException extends EntityNotFoundException {

    public NotExistUserException() {
        super(ErrorCode.USER_NOT_FOUND.getMessage() , ErrorCode.USER_NOT_FOUND);
    }
}
