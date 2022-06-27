package com.devcourse.eggmarket.domain.evaluation.exception;

import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import com.devcourse.eggmarket.global.error.exception.InvalidValueException;

public class InvalidScoreException extends InvalidValueException {

    public InvalidScoreException(int score) {
        super(score + "는 유효하지 않은 점수입니다. ", ErrorCode.SCORE_INPUT_INVALID);
    }
}
