package com.devcourse.eggmarket.domain.user.converter;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Simple;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.UpdateProfile;
import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public UserConverter() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public User saveToUser(UserRequest.Save userRequest) {
        return User.builder()
            .nickName(userRequest.nickName())
            .phoneNumber(userRequest.phoneNumber())
            .password(passwordEncoder.encode(userRequest.password()))
            .role("USER")
            .build();
    }

    public UserResponse.Basic convertToBasic(User user) {
        return UserResponse.Basic.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .mannerTemperature(user.getMannerTemperature())
            .role(user.getRole().toString())
            .imagePath(user.getImagePath())
            .build();
    }

    public UserResponse.FindNickName convertToNickName(User user) {
        return UserResponse.FindNickName.builder()
            .nickName(user.getNickName())
            .build();
    }

    public Simple convertToSimple(User user) {
        return Simple.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .mannerTemperature(user.getMannerTemperature())
            .build();
    }

    public UpdateProfile convertToUpdateProfile(User user) {
        return UserResponse.UpdateProfile.builder()
            .id(user.getId())
            .imagePath(user.getImagePath())
            .build();
    }
}
