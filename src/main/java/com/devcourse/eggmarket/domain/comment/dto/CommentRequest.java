package com.devcourse.eggmarket.domain.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CommentRequest {

    public record Save(
        @NotBlank(message = "댓글을 입력해주세요")
        @Size(max = 500)
        String content
    ) {

    }

    public record Update(
        @NotBlank(message = "댓글을 입력해주세요")
        @Size(max = 500)
        String content
    ) {

    }
}
