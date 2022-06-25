package com.devcourse.eggmarket.domain.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public class UserResponse {

    public record Basic(
        Long id,
        String nickName,
        float mannerTemperature,
        String role,
        String imagePath
    ) {

        @Builder
        public Basic {
        }
    }

    public record FindNickName(
        String nickName
    ) {

        @Builder
        public FindNickName {
        }
    }

}
