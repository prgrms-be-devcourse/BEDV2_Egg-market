package com.devcourse.eggmarket.domain.evaluation.model;

import com.devcourse.eggmarket.domain.evaluation.exception.InvalidScoreException;
import java.util.Arrays;

public enum Score {
    SCORE_RANK_1(-0.3F, 1),
    SCORE_RANK_2(-0.1F, 2),
    SCORE_RANK_3(0F, 3),
    SCORE_RANK_4(0.2F, 4),
    SCORE_RANK_5(0.5F, 5);

    private final float temperatureScore;
    private final int rankScore;

    Score(float score, int rankScore) {
        this.temperatureScore = score;
        this.rankScore = rankScore;
    }

    public float getTemperatureScore() {
        return this.temperatureScore;
    }

    public static Score findByRankScore(int rankScore) {
        return Arrays.stream(Score.values())
            .filter(score -> score.equalsRankScore(rankScore))
            .findFirst()
            .orElseThrow(() -> new InvalidScoreException(rankScore));
    }

    private boolean equalsRankScore(int rankScore) {
        return (this.rankScore == rankScore);
    }
}
