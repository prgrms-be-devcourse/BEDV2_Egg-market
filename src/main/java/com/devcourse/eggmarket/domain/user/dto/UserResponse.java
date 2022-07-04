package com.devcourse.eggmarket.domain.user.dto;

import lombok.Builder;

public class UserResponse {

    public record Basic(
        Long id,
        String nickName,
        float mannerTemperature,
        String role,
        String imagePath
    ) {

        @Builder
        public Basic {
        }
    }

    public record FindNickName(
        String nickName
    ) {

        @Builder
        public FindNickName {
        }
    }

    public record Update(
        Long id,
        String phoneNumber,
        String nickName
    ) {

        @Builder
        public Update {
        }
    }

    public record MannerTemperature(
        Long id,
        String nickName,
        float mannerTemperature
    ) {

        @Builder
        public MannerTemperature {
        }
    }

}
