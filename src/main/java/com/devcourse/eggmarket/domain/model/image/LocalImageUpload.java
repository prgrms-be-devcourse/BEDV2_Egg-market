package com.devcourse.eggmarket.domain.model.image;

import com.devcourse.eggmarket.domain.model.image.exception.FileDeleteFailException;
import com.devcourse.eggmarket.domain.model.image.exception.ImageFileUploadException;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class LocalImageUpload implements ImageUpload {

    private final ImageLocalPathProperties pathProperties;

    public LocalImageUpload(ImageLocalPathProperties pathProperties) {
        this.pathProperties = pathProperties;
    }

    @Override
    public String upload(Image image) {
        String path = imgPath(image);

        try {
            FileUtils.writeByteArrayToFile(new File(path), image.getContents());
        } catch (IOException e) {
            throw new ImageFileUploadException(e);
        }
        return path;
    }

    private String imgPath(Image image) {
        return switch (image.getType()) {
            case POST -> image.pathTobeStored(pathProperties.postImagePath());
            case PROFILE -> image.pathTobeStored(pathProperties.profileImagePath());
        };
    }

    @Override
    public void deleteFile(String imagePath) {
        Path filePath = Path.of(imagePath);

        try {
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            throw new FileDeleteFailException("삭제하려는 파일/디렉토리가 없습니다", e);
        } catch (DirectoryNotEmptyException e) {
            throw new FileDeleteFailException("삭제하려는 디렉토리가 비어있지 않습니다", e);
        } catch (IOException e) {
            throw new FileDeleteFailException("이미지 삭제 실패하였습니다", e);
        }
    }
}
