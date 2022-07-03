package com.devcourse.eggmarket.domain.model;

import com.devcourse.eggmarket.domain.model.image.ProfileImageFile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ProfileImageFileTest {

    @Test
    @DisplayName("멀티파트와 유저아이디가 주어지면 프로필이미지 생성 경로를 생성할 수 있다")
    public void createFilePath() {
        MultipartFile multipartFile = new MockMultipartFile(
            "img.png",
            "src/test/java/com/devcourse/eggmarket/domain/model/image/data/img.png",
            "image/png",
            new byte[]{1});
        String basePath = "profile";

        ProfileImageFile profileImageFile = ProfileImageFile.toImage(1L, multipartFile);

        Assertions.assertThat(
            profileImageFile.pathTobeStored(basePath)
        ).isEqualTo("profile/1.png");
    }
}
