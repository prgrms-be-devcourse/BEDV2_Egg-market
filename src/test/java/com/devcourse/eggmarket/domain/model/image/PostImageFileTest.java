package com.devcourse.eggmarket.domain.model.image;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class PostImageFileTest {

    private MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get(
            "src/test/java/com/devcourse/eggmarket/domain/model/image/data/img.png");
        String name = "img.png";
        String originalFileName = "img.png";
        String contentType = "image/png";

        InputStream inputStream = new FileInputStream(path.toString());
        this.multipartFile = new MockMultipartFile(name, path.toString(), contentType, inputStream);
    }

    @Test
    @DisplayName("Post id 와 해당 파일의 순서와 Multipartfile 이 주어지면, post id와 순서로 유니크한 경로를 만들어낼 수 있는 PostImage 를 생성한다")
    public void createPostImage() {
        PostImageFile postImage = PostImageFile.toImage(1, multipartFile, 2);

        Assertions.assertThat(postImage.pathTobeStored("foo"))
            .isEqualTo("foo/1_2.png");
    }
}