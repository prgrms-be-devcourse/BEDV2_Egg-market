package com.devcourse.eggmarket.domain.comment.dto;

import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {

    public record Comments(
        List<CommentsElement> comments
    ) {

    }

    public record CommentsElement(
        Long id,
        UserResponse writer,
        String content,
        LocalDateTime createdAt
    ) {

    }
}
