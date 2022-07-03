package com.devcourse.eggmarket.domain.evaluation.model;

import com.devcourse.eggmarket.domain.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evaluation")
public class Evaluation extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "reviewee_id")
    private Long revieweeId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "score")
    private int score;

    @Column(name = "content")
    private String content;

    private float calculateTemperature(final int score) {
        Score temperatureScore = Score.findByRankScore(score);

        return temperatureScore.getTemperatureScore();
    }

    @Builder
    public Evaluation(Long id, Long reviewerId, Long revieweeId, Long postId, int score,
        String content) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
        this.postId = postId;
        this.score = score;
        this.content = content;
    }

    public Evaluation(Long reviewerId, Long revieweeId, Long postId, int score,
        String content) {
        this(null, reviewerId, revieweeId, postId, score, content);
    }

    public Long getId() {
        return this.id;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public Long getRevieweeId() {
        return revieweeId;
    }

    public String getContent() {
        return this.content;
    }

    public float getTemperatureScore() {
        return calculateTemperature(this.score);
    }
}
