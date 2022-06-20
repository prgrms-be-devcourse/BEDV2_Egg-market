package com.devcourse.eggmarket.domain.model.image;

import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageLocalPathPropertiesTest {

    @Autowired
    private ImageLocalPathProperties imageLocalPathProperties;

    @Test
    @DisplayName("설정 저장정보로부터 프로젝트 기본 경로를 가져온다")
    public void basePath() {
        String property = System.getProperty("user.dir");
        String basePath = imageLocalPathProperties.getBase();

        Assertions.assertThat(basePath).isEqualTo(property);
    }

    @Test
    @DisplayName("설정 정보로부터 포스트 이미지 저장경로를 생성한다")
    public void postFilePath() {
        String basePath = imageLocalPathProperties.getBase();
        String postPath = imageLocalPathProperties.getType().getPost();
        String concatenated = FilenameUtils.concat(basePath, postPath);

        String postImagePath = imageLocalPathProperties.postImagePath();

        Assertions.assertThat(postImagePath)
            .isEqualTo(concatenated);
    }
}