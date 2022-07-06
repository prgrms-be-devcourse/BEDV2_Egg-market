package com.devcourse.eggmarket.domain.evaluation.service;

import com.devcourse.eggmarket.domain.evaluation.converter.EvaluationConverter;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationResponse;
import com.devcourse.eggmarket.domain.evaluation.model.Evaluation;
import com.devcourse.eggmarket.domain.evaluation.repository.EvaluationRepository;
import com.devcourse.eggmarket.domain.user.exception.NotExistUserException;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    private final UserService userService;

    private final EvaluationConverter evaluationConverter;

    public EvaluationService(
        EvaluationRepository evaluationRepository,
        UserService userService,
        EvaluationConverter evaluationConverter) {
        this.evaluationRepository = evaluationRepository;
        this.userService = userService;
        this.evaluationConverter = evaluationConverter;
    }

    @Transactional
    public Long save(EvaluationRequest.Save evaluationDto) {
        Evaluation evaluation = evaluationConverter.saveToEvaluation(evaluationDto);
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        User reveiwee = userService.getById(evaluationDto.revieweeId());

        reveiwee.updateMannerTemperature(evaluation.getTemperatureScore());

        return savedEvaluation.getId();
    }

    public EvaluationResponse getByReviewerId(Long reviewerId) {
        if (!evaluationRepository.existsByReviewerId(reviewerId)) {
            throw new NotExistUserException();
        }
        Evaluation evaluation = evaluationRepository.getByReviewerId(reviewerId);

        User reviewer = userService.getById(evaluation.getReviewerId());

        return evaluationConverter.convertToEvaluationResponse(reviewer.getNickName(), evaluation.getContent());
    }

    public EvaluationResponse getByRevieweeId(Long revieweeId) {
        if (!evaluationRepository.existsByRevieweeId(revieweeId)) {
            throw new NotExistUserException();
        }
        Evaluation evaluation = evaluationRepository.getByRevieweeId(revieweeId);

        User reviewee = userService.getById(evaluation.getRevieweeId());

        return evaluationConverter.convertToEvaluationResponse(reviewee.getNickName(), evaluation.getContent());
    }

    @Transactional
    public void delete(Long reviewerId) {
        if (!evaluationRepository.existsByReviewerId(reviewerId)) {
            return ;
        }
        Evaluation evaluation = evaluationRepository.getByReviewerId(reviewerId);
        evaluationRepository.deleteById(evaluation.getId());
    }
}
