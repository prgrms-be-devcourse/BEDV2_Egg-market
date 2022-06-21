package com.devcourse.eggmarket.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.devcourse.eggmarket.domain.user.model.Users;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseEntityTest {

  @Autowired
  UserRepository userRepository;

  @AfterEach
  void cleanUp(){
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("유저 Auditing 테스트")
  void user_test() {
    // given
    LocalDateTime now = LocalDateTime.of(2022, 6, 20, 0, 0, 0);
    Users user = Users.builder()
        .nickName("mark")
        .password("Password1!")
        .phoneNumber("01012345678")
        .role("USER")
        .build();

    // when
    Users savedUser = userRepository.save(user);

    // then
    assertThat(savedUser.getCreatedAt()).isAfter(now);
    assertThat(savedUser.getUpdatedAt()).isAfter(now);
  }

  @Test
  @DisplayName("유저 update Auditing 테스트")
  void user_update_test() {
    // given
    Users user = Users.builder()
        .nickName("mark")
        .password("Password1!")
        .phoneNumber("01012345678")
        .role("USER")
        .build();
    Users savedUser = userRepository.save(user);

    // when
    savedUser.changeNickName("egg");
    Users changedUser = userRepository.save(savedUser);

    // then
    assertThat(changedUser.getUpdatedAt()).isAfter(changedUser.getCreatedAt());
  }

}

