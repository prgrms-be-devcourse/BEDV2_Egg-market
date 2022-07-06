package com.devcourse.eggmarket.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    void save() {
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
        assertThat(userRepository.findById(result).isPresent())
            .isTrue();
    }

    @Test
    void getByUsername() {
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
    void update() {
        //Given
        UserRequest.Update userRequest = new Update("01011111111", "updateNick");
        UserResponse.Update expectResponse = new UserResponse.Update(user.getId(),
            userRequest.phoneNumber(), userRequest.nickName());

        //When
        UserResponse.Update result = userService.update(user, userRequest);

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResponse);

    }

    @Test
    void getUser() {
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
    void deleteById() {
        //When
        Long userId = userService.delete(user);

        //Then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    void getUserName() {
        //Given
        UserResponse.FindNickName expectResult = UserResponse.FindNickName.builder()
            .nickName(user.getNickName()).build();

        //When
        UserResponse.FindNickName result = userService.getUserName(user.getPhoneNumber());

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResult);
    }

    @Test
    void updatePassword() {
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
    void getMannerTemperature() {
        //Given
        UserResponse.MannerTemperature expectResponse = UserResponse.MannerTemperature.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .mannerTemperature(user.getMannerTemperature())
            .build();

        //When
        UserResponse.MannerTemperature result = userService.getMannerTemperature(user.getId());

        //Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectResponse);
    }

}