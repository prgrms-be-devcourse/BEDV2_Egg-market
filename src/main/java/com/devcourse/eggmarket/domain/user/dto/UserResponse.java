package com.devcourse.eggmarket.domain.user.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public record UserResponse(
    Long id,
    String nickName,
    float mannerTemperature,
    String role,
    String imagePath
) {

    @Builder
    public UserResponse {
    }

}
