package com.devcourse.eggmarket.domain.model.image;

public interface ImageUpload {

    String upload(Image image);

    void deleteFile(String imagePath);
}
