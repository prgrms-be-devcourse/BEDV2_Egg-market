package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.model.image.ImageFile;
import com.devcourse.eggmarket.domain.model.image.PostImageFile;
import java.nio.charset.StandardCharsets;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageStub {

    private ImageStub() {
    }

    private static final String mockImage = "image";

    public static MockMultipartFile image1() {
        return new MockMultipartFile(
            "image",
            "img.png",
            "image/png",
            mockImage.getBytes(StandardCharsets.UTF_8)
        );
    }
    public static ImageFile uploadImage1(Long postId) {
        return PostImageFile.toImage(postId, new MockMultipartFile("image1",
            "1.png",
            "image/png",
            mockImage.getBytes(StandardCharsets.UTF_8)), 1
        );

    }

    public static ImageFile uploadImage2(Long postId) {
        return PostImageFile.toImage(postId,
            new MockMultipartFile("image2",
                "2.png",
                "image/png",
                mockImage.getBytes(StandardCharsets.UTF_8)),
            2);
    }
}
