package com.devcourse.eggmarket.domain.evaluation.dto;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_BLANK_CONTENT;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class EvaluationRequest {

    public record Save(

        @Positive
        Long reviewerId,

        @Positive
        Long revieweeId,

        @Positive
        Long postId,

        @Range(min = 1, max = 5)
        int score,

        @NotBlank(message = NOT_BLANK_CONTENT)
        @Size(min = 1, max = 500, message = "본문은 1 ~ 500자 사이여야 합니다.")
        String content
    ) {

    }
}
