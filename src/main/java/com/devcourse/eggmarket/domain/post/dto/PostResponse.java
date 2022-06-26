package com.devcourse.eggmarket.domain.post.dto;

import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
    Long id,
    UserResponse seller,
    int price,
    String title,
    String content,
    String postStatus,
    String category,
    LocalDateTime createAt,
    int attentionCount,
    int commentCount,
    boolean likeOfMe,
    List<String> imagePaths
) {

}
