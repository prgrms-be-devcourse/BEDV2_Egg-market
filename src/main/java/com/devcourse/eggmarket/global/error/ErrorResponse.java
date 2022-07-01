package com.devcourse.eggmarket.global.error;

import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import java.util.List;
import org.springframework.validation.BindingResult;

public class ErrorResponse {

    private final String code;
    private final List<ErrorMessage> errors;

    public ErrorResponse(String code,
        List<ErrorMessage> errors) {
        this.code = code;
        this.errors = errors;
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code.getCode(),
            List.of(new ErrorMessage(code.getMessage()))
        );
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(
            ErrorCode.INVALID_INPUT.getCode(),
            ErrorField.of(bindingResult));
    }

    public String getCode() {
        return code;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
