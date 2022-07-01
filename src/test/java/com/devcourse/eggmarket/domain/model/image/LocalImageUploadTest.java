package com.devcourse.eggmarket.domain.model.image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class LocalImageUploadTest {

    @Autowired
    ImageLocalPathProperties pathProperties;

    private ImageUpload imageUpload;
    private ImageFile postImageFile;

    private String pathSaved;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get(
            "src/test/java/com/devcourse/eggmarket/domain/model/image/data/img.png");
        Path savedPath = Paths.get(
            "src/test/java/com/devcourse/eggmarket/domain/model/image/data/img2.png");

        String name = "img.png";
        String name2 = "img2.png";

        String contentType = "image/png";

        InputStream inputStream = new FileInputStream(path.toString());
        InputStream inputStream2 = new FileInputStream(savedPath.toString());
        MultipartFile multipartFile = new MockMultipartFile(name, path.toString(), contentType,
            inputStream);
        MultipartFile multipartFile2 = new MockMultipartFile(name2, savedPath.toString(),
            contentType, inputStream2);

        PostImageFile savedPostImage = PostImageFile.toImage(1, multipartFile2, 4);

        this.postImageFile = PostImageFile.toImage(1, multipartFile, 2);
        this.imageUpload = new LocalImageUpload(pathProperties);
        this.pathSaved = this.imageUpload.upload(savedPostImage);
    }

    @Test
    @DisplayName("PostImage 를 서버에 저장한다")
    public void uploadTest() {
        String uploadedPath = imageUpload.upload(postImageFile);

        Assertions.assertThat(Files.exists(Path.of(uploadedPath))).isTrue();
    }

    @Test
    @DisplayName("주어진 이미지 경로에 있는 이미지 파일을 삭제한다")
    public void deleteTest() {
        imageUpload.deleteFile(pathSaved);

        Assertions.assertThat(Files.exists(Path.of(pathSaved))).isFalse();
    }
}