package com.devcourse.eggmarket.domain.evaluation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

import com.devcourse.eggmarket.domain.evaluation.converter.EvaluationConverter;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest.Save;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationResponse;
import com.devcourse.eggmarket.domain.evaluation.model.Evaluation;
import com.devcourse.eggmarket.domain.evaluation.repository.EvaluationRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @InjectMocks
    private EvaluationService evaluationService;

    @Mock
    private UserService userService;

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private EvaluationConverter evaluationConverter;

    @Test
    @DisplayName("평가 저장 테스트")
    void save() {
        EvaluationRequest.Save request = new Save(
            1L, 2L, 3L, 5, "감사합니다."
        );

        Evaluation evaluation = Evaluation.builder()
            .reviewerId(1L)
            .revieweeId(2L)
            .postId(3L)
            .score(5)
            .content("감사합니다")
            .build();

        User user = User.builder()
            .phoneNumber("010-1234-5678")
            .nickName("json")
            .password("1q2w3e4r!")
            .role("USER")
            .imagePath(null)
            .build();

        given(evaluationConverter.saveToEvaluation(request)).willReturn(evaluation);
        given(evaluationRepository.save(any(Evaluation.class))).willReturn(evaluation);
        given(userService.getById(any(Long.class))).willReturn(user);

        evaluationService.save(request);

        then(evaluationConverter).should(times(1)).saveToEvaluation(request);
        then(evaluationRepository).should(times(1)).save(evaluation);
        then(userService).should(times(1)).getById(any(Long.class));
    }

    @Test
    @DisplayName("리뷰어 아이디로 조회 테스트")
    void getByReviewerId() {
        Evaluation evaluation = Evaluation.builder()
            .reviewerId(1L)
            .revieweeId(2L)
            .postId(3L)
            .score(5)
            .content("수고하셨습니다")
            .build();

        User reviewer = User.builder()
            .phoneNumber("010-1234-5678")
            .nickName("json")
            .password("1q2w3e4r!")
            .role("USER")
            .imagePath(null)
            .build();

        EvaluationResponse evaluationResponse = new EvaluationResponse(
            "json",
            "수고하셨습니다."
        );

        given(evaluationRepository.existsByReviewerId(any(Long.class))).willReturn(true);
        given(evaluationRepository.getByReviewerId(any(Long.class))).willReturn(evaluation);
        given(userService.getById(any(Long.class))).willReturn(reviewer);
        given(evaluationConverter.convertToEvaluationResponse(any(String.class), any(String.class)))
            .willReturn(evaluationResponse);

        evaluationService.getByReviewerId(1L);

        then(evaluationRepository).should(times(1)).getByReviewerId(any(Long.class));
    }

    @Test
    @DisplayName("리뷰이 아이디로 조회 테스트")
    void getByRevieweeId() {
        Evaluation evaluation = Evaluation.builder()
            .reviewerId(1L)
            .revieweeId(2L)
            .postId(3L)
            .score(5)
            .content("감사합니다")
            .build();

        User reviewee = User.builder()
            .phoneNumber("010-5678-1234")
            .nickName("xml")
            .password("1q2w3e4r!")
            .role("USER")
            .imagePath(null)
            .build();

        EvaluationResponse evaluationResponse = new EvaluationResponse(
            "xml",
            "감사합니다."
        );

        given(evaluationRepository.existsByRevieweeId(any(Long.class))).willReturn(true);
        given(evaluationRepository.getByRevieweeId(any(Long.class))).willReturn(evaluation);
        given(userService.getById(any(Long.class))).willReturn(reviewee);
        given(evaluationConverter.convertToEvaluationResponse(any(String.class), any(String.class)))
            .willReturn(evaluationResponse);

        evaluationService.getByRevieweeId(1L);

        then(evaluationRepository).should(times(1)).getByRevieweeId(any(Long.class));
    }

    @Test
    @DisplayName("평가 삭제 테스트")
    void deleteById() {
        Evaluation evaluation = Evaluation.builder()
            .id(4L)
            .reviewerId(1L)
            .revieweeId(2L)
            .postId(3L)
            .score(5)
            .content("감사합니다")
            .build();

        given(evaluationRepository.existsByReviewerId(any(Long.class))).willReturn(true);
        given(evaluationRepository.getByReviewerId(any(Long.class))).willReturn(evaluation);
        willDoNothing().given(evaluationRepository).deleteById(any(Long.class));

        evaluationService.delete(any(Long.class));

        then(evaluationRepository).should(times(1)).deleteById(any(Long.class));
    }
}
