package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.user.model.User;

public class UserStub {

    private UserStub() {
    }

    public static User entity() {
        return User.builder()
            .nickName("test")
            .password("asdfg123!@")
            .phoneNumber("01000000000")
            .role("USER")
            .build();
    }
}
