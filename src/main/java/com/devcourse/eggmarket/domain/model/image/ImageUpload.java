package com.devcourse.eggmarket.domain.model.image;

public interface ImageUpload {

    String upload(ImageFile image);

    void deleteFile(String imagePath);
}
