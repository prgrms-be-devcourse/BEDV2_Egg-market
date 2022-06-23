package com.devcourse.eggmarket.domain.post.model;

import com.devcourse.eggmarket.domain.post.exception.DuplicateBuyerException;
import com.devcourse.eggmarket.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

  @Test
  @DisplayName("판매글에 이미 구매자가 등록되어 있는 경우 예외 발생")
  void alreadyRegisteredBuyerTest() {
    User otherBuyer = User.builder()
        .nickName("abc")
        .password("asdasd123!")
        .phoneNumber("01010101010")
        .role("ADMIN")
        .build();
    Post post = Post.builder()
        .title("title")
        .content("content")
        .build();

    Assertions.assertThatExceptionOfType(DuplicateBuyerException.class)
        .isThrownBy(() -> post.setBuyer(otherBuyer));
  }

}