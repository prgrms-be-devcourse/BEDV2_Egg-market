package com.devcourse.eggmarket.domain.model.image.exception;

public class ImageFileUploadException extends RuntimeException {

    public ImageFileUploadException(Throwable cause) {
        super("이미지 파일 생성에 실패하였습니다", cause);
    }
}
