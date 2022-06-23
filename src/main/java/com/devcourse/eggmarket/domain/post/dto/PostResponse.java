package com.devcourse.eggmarket.domain.post.dto;

import java.time.LocalDateTime;

public record PostResponse(
    Long id,
    Long sellerId,
    Long buyerId,
    int price,
    String title,
    String content,
    String postStatus,
    String category,
    LocalDateTime createAt,
    LocalDateTime updatedAt
) {

}
