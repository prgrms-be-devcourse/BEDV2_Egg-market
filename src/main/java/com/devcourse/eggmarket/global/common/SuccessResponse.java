package com.devcourse.eggmarket.global.common;

public class SuccessResponse<T> {

    private final T data;

    public SuccessResponse() {
        this.data = null;
    }

    public SuccessResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
