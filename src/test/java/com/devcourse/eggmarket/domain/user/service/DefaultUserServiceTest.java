package com.devcourse.eggmarket.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import static org.assertj.core.api.Assertions.*;

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

        UserResponse.Basic expectResponse = UserResponse.Basic.builder()
            .nickName(newUser.getNickName())
            .mannerTemperature(36.5F)
            .role(newUser.getRole().toString())
            .build();

        UserRequest.Save saveRequest = new UserRequest.Save(newUser.getPhoneNumber(),
            newUser.getNickName(), newUser.getPassword(), false, null);

        //When
        UserResponse.Basic result = userService.save(saveRequest);

        //Then
        assertThat(result).usingRecursiveComparison().ignoringFields("id")
            .isEqualTo(expectResponse);

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
    }

    @Test
    void getUser() {
        //When
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        User foundUser = userService.getUser(securityContext.getAuthentication());

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

}