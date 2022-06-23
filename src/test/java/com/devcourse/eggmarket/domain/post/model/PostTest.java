package com.devcourse.eggmarket.domain.post.model;

import com.devcourse.eggmarket.domain.post.exception.AlreadyCompletedException;
import com.devcourse.eggmarket.domain.post.exception.NotExistBuyerException;
import com.devcourse.eggmarket.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

  @Test
  @DisplayName("판매글이 판매완료 상태에서 변경시도시 예외 발생")
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

    post.updatePurchaseInfo(PostStatus.COMPLETED, otherBuyer);
    Assertions.assertThatExceptionOfType(AlreadyCompletedException.class)
        .isThrownBy(() -> post.updatePurchaseInfo(PostStatus.COMPLETED, otherBuyer));
  }

  @Test
  @DisplayName("구매자가 등록되지 않은 상태에서 상태 변경 시도시 예외 발생")
  void NotExistBuyerTest() {
    Post post = Post.builder()
        .title("title")
        .content("content")
        .build();

    Assertions.assertThatExceptionOfType(NotExistBuyerException.class)
        .isThrownBy(() -> post.updatePurchaseInfo(PostStatus.COMPLETED, null));
  }

}