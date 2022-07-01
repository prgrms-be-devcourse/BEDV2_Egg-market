package com.devcourse.eggmarket.domain.comment.exception;

import com.devcourse.eggmarket.global.error.exception.BusinessException;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;

public class CommentNotAllowedPostException extends BusinessException {

    public CommentNotAllowedPostException(ErrorCode errorCode) {
        super(errorCode);
    }
}
