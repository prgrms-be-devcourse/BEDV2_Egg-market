package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Basic;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.FindNickName;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Simple;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.UpdateProfile;
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

    public static User imagePathEntity() {
        return new User(2L,
            "010-2234-5678",
            "tests",
            "test1234!@#$s",
            36.5F,
            "http://example.com/profile/2.png",
            UserRole.USER);
    }
    public static Save saveRequest() {
        return new UserRequest.Save(
            "010-1234-5678",
            "test",
            "Test1234!@#$",
            false,
            null
        );
    }

    public static Save saveRequest2() {
        return new UserRequest.Save(
            "010-1334-5678",
            "buyer",
            "Test1234!@#$",
            false,
            null
        );
    }

    public static Basic basicResponse(User user) {
        return UserResponse.Basic.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .imagePath(user.getImagePath())
            .role(String.valueOf(user.getRole()))
            .mannerTemperature(user.getMannerTemperature())
            .build();
    }

    public static FindNickName nickNameResponse(User user) {
        return new FindNickName(user.getNickName());
    }

    public static Simple simpleResponse(User user) {
        return Simple.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .mannerTemperature(user.getMannerTemperature())
            .build();
    }

    public static UpdateProfile updateProfileResponse(User user) {
        return UserResponse.UpdateProfile.builder()
            .id(user.getId())
            .imagePath(user.getImagePath())
            .build();
    }
}
