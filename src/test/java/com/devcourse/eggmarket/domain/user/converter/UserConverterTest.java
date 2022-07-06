package com.devcourse.eggmarket.domain.user.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.devcourse.eggmarket.domain.stub.UserStub;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserConverterTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserConverter userConverter = new UserConverter();

    @Test
    @DisplayName("UserRequest.save -> User entity 변환 테스트")
    void saveToUserTest() {
        UserRequest.Save request = UserStub.saveRequest();
        User want = UserStub.entity();

        User got = userConverter.saveToUser(request);

        assertThat(got)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .ignoringFields("password")
            .isEqualTo(want);
        assertThat(passwordEncoder.matches(want.getPassword(), got.getPassword())).isTrue();
    }

    @Test
    @DisplayName("User -> UserResponse.Basic 변환 테스트")
    void convertToUserResponseBasicTest() {
        User request = UserStub.entity();
        UserResponse.Basic want = UserStub.basicResponse(request);

        UserResponse.Basic got = userConverter.convertToUserResponseBasic(request);

        assertThat(got).usingRecursiveComparison().isEqualTo(want);
    }

    @Test
    @DisplayName("User -> UserResponse.findNickName 변환 테스트")
    void convertToUserFindNickNameTest() {
        User request = UserStub.entity();
        UserResponse.FindNickName want = UserStub.nickNameResponse(request);

        UserResponse.FindNickName got = userConverter.convertToUserFindNickName(request);

        assertThat(got).usingRecursiveComparison().isEqualTo(want);
    }

    @Test
    @DisplayName("User -> UserResponse.MannerTemperature 변환 테스트")
    void convertToMannerTemp() {
        User request = UserStub.entity();
        UserResponse.MannerTemperature want = UserStub.temperatureResponse(request);

        UserResponse.MannerTemperature got = userConverter.convertToMannerTemp(request);

        assertThat(got).usingRecursiveComparison().isEqualTo(want);
    }

    @Test
    @DisplayName("User -> UserResponse.UpdateProfile 변환 테스트")
    void convertToUserUpdateProfileTest() {
        User request = UserStub.imagePathEntity();
        UserResponse.UpdateProfile want = UserStub.updateProfileResponse(request);

        UserResponse.UpdateProfile got = userConverter.convertToUserUpdateProfile(request);

        assertThat(got).usingRecursiveComparison().isEqualTo(want);
    }
}