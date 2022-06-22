package com.devcourse.eggmarket.domain.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserResponse(
    Long id,
    String nickName,
    float mannerTemperature,
    MultipartFile profileImage,
    String role
) {

}
