package com.devcourse.eggmarket.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.devcourse.eggmarket.domain.stub.ImageStub;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Profile;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Simple;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.UpdateProfile;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class DefaultUserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .nickName("test1")
            .phoneNumber("01012345678")
            .password("Password!1")
            .role("USER")
            .build();

        userRepository.save(user);
    }

    @AfterEach
    void clearUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 정보 DB 저장 테스트")
    void saveTest() {
        //Given
        User newUser = User.builder()
            .nickName("test2")
            .phoneNumber("01011111111")
            .password("Password!2")
            .role("USER")
            .build();

        UserRequest.Save saveRequest = new UserRequest.Save(newUser.getPhoneNumber(),
            newUser.getNickName(), newUser.getPassword(), false, null);

        //When
        Long result = userService.save(saveRequest);

        //Then
        assertThat(userRepository.findById(result))
            .isPresent();
    }

    @Test
    @DisplayName("유저 정보 조회 테스트")
    void getByUsernameTest() {
        //Given
        UserResponse.Basic expectResponse = UserResponse.Basic.builder()
            .nickName(user.getNickName())
            .mannerTemperature(36.5F)
            .role(user.getRole().toString())
            .build();

        //When
        UserResponse.Basic result = userService.getByUsername(user.getNickName());

        //Then
        assertThat(result).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("유저 정보만 변경 테스트")
    void updateUserInfoTest() {
        //Given
        UserRequest.Update userRequest = new Update("01011111111", "updateNick");
        Long expectResponse = user.getId();

        //When
        Long result = userService.updateUserInfo(user, userRequest);

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResponse);

    }

    @Test
    @DisplayName("유저 프로필 변경 테스트")
    void updateUserProfileTest() {
        UserRequest.Profile userRequest = new Profile(ImageStub.image1());
        UserResponse.UpdateProfile expectResponse = UserResponse.UpdateProfile.builder()
            .id(user.getId())
            .build();

        UserResponse.UpdateProfile result = userService.updateUserProfile(user, userRequest);

        assertThat(result.id()).usingRecursiveComparison().isEqualTo(expectResponse.id());
    }

    @Test
    @DisplayName("닉네임을 기준으로 유저 조회 테스트")
    void getUserTest() {
        //When
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        User foundUser = userService.getUser(securityContext.getAuthentication().getName());

        //Then
        assertThat(foundUser).usingRecursiveComparison()
            .ignoringFields("id", "createdAt", "updatedAt").isEqualTo(user);
    }

    @Test
    @DisplayName("유저 아이디를 기준으로 삭제 테스트")
    void deleteByIdTest() {
        //When
        Long userId = userService.delete(user);

        //Then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("유저 닉네임 조회 기능 테스트")
    void getUserNameTest() {
        //Given
        UserResponse.FindNickName expectResult = UserResponse.FindNickName.builder()
            .nickName(user.getNickName()).build();

        //When
        UserResponse.FindNickName result = userService.getUserName(user.getPhoneNumber());

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResult);
    }

    @Test
    @DisplayName("패스워드 변경 테스트")
    void updatePasswordTest() {
        //Given
        UserRequest.ChangePassword userRequest = new UserRequest.ChangePassword(
            "NewPass!1"
        );

        //When
        boolean result = userService.updatePassword(user, userRequest);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유저 간단 조회 테스트")
    void getUserBySimpleTest() {
        //Given
        Simple expectResponse = Simple.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .mannerTemperature(user.getMannerTemperature())
            .build();

        //When
        Simple result = userService.getUserBySimple(user.getId());

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResponse);
    }

}