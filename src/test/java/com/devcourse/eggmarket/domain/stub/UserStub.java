package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.model.UserRole;

public class UserStub {

    private UserStub() {
    }

    public static User entity() {
        return new User(1L,
            "010-1234-5678",
            "test",
            "test1234!@#$",
            36.5F,
            null,
            UserRole.USER);
    }

    public static User entity2() {
        return new User(2L,
            "010-2234-5678",
            "tests",
            "test1234!@#$s",
            36.5F,
            null,
            UserRole.USER);
    }
}
