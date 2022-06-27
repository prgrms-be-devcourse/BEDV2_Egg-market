package com.devcourse.eggmarket.domain.evaluation.dto;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_CONTENT;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class EvaluationRequest {

    public record Save(
        Long reviewerId,

        Long revieweeId,

        Long post_id,

        @Positive(message = "점수는 1점에서 5점 사이여야 합니다.")
        int score,

        @NotBlank(message = NOT_BLANK_CONTENT)
        @Size(min = 1, max = 500, message = "본문은 1 ~ 500자 사이여야 합니다.")
        String content
    ) {

    }
}
