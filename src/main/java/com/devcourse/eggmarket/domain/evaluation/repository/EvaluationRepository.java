package com.devcourse.eggmarket.domain.evaluation.repository;

import com.devcourse.eggmarket.domain.evaluation.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Evaluation getByReviewerId(Long reviewerId);

    Evaluation getByRevieweeId(Long revieweeId);

//    List<Evaluation> findAllByRevieweeId(Long revieweeId);

    boolean existsByReviewerId(Long reviewerId);

    boolean existsByRevieweeId(Long revieweeid);
}
// 평가에 관한 히스토리?