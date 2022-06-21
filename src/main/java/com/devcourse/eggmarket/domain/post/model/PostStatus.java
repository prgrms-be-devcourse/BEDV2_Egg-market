package com.devcourse.eggmarket.domain.post.model;

public enum PostStatus {
  SALE("판매중"),
  COMPLETED("판매완료");

  private final String value;

  PostStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
