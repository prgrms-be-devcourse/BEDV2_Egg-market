package com.devcourse.eggmarket.global.error;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorField extends ErrorMessage {

    private final String field;
    private final String value;

    public ErrorField(String field, String value, String message) {
        super(message);
        this.field = field;
        this.value = value;
    }

    public static List<ErrorMessage> of(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
            .map(error ->
                new ErrorField(
                    ((FieldError) error).getField(),
                    String.valueOf(rejectedValue(((FieldError) error).getRejectedValue())),
                    error.getDefaultMessage()))
            .collect(Collectors.toList());
    }

    private static Object rejectedValue(Object value) {
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return ((Collection<?>) value).size();
        }
        return value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
