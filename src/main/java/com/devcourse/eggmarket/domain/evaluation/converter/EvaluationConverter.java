package com.devcourse.eggmarket.domain.evaluation.converter;

import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationResponse;
import com.devcourse.eggmarket.domain.evaluation.model.Evaluation;
import org.springframework.stereotype.Component;

@Component
public class EvaluationConverter {

    public Evaluation saveToEvaluation(EvaluationRequest.Save evaluationRequest) {
        return Evaluation.builder()
            .postId(evaluationRequest.post_id())
            .revieweeId(evaluationRequest.revieweeId())
            .reviewerId(evaluationRequest.reviewerId())
            .score(evaluationRequest.score())
            .content(evaluationRequest.content())
            .build();
    }

    public EvaluationResponse convertToEvaluationResponse(String revieweeName, String content) {
        return new EvaluationResponse(revieweeName, content);
    }
}
