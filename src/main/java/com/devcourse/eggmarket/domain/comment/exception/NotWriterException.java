package com.devcourse.eggmarket.domain.comment.exception;

import com.devcourse.eggmarket.global.error.exception.BusinessException;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;

public class NotWriterException extends BusinessException {

    public NotWriterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
