package com.devcourse.eggmarket.global.common;

public class SuccessResponse<T> {

    private final T data;

    public SuccessResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
