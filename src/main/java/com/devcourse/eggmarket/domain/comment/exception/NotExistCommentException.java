package com.devcourse.eggmarket.domain.comment.exception;

import com.devcourse.eggmarket.global.error.exception.EntityNotFoundException;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;

public class NotExistCommentException extends EntityNotFoundException {

    public NotExistCommentException(String message,
        ErrorCode errorCode) {
        super(message, errorCode);
    }

    public NotExistCommentException(String message) {
        super(message);
    }
}
