package com.devcourse.eggmarket.domain.post.model;

public enum Category {
  DIGITAL("디지털 기기"),
  HOUSEHOLD_APPLIANCES("생활 가전"),
  FURNITURE("가구/인테리어"),
  INFANT_CHILD("유아동"),
  CHILDREN_BOOKS("유아도서"),
  WOMEN_VARIOUS("여성 잡화"),
  WOMEN_CLOTHING("여성 의류"),
  MAN_CLOTHING_VARIOUS("남성 패션/잡화"),
  GAME_HOBBY("게임/취미"),
  BEAUTY("뷰티/미용"),
  PET_GOODS("반려동물용품"),
  BOOK_TICKET_DISCOGRAPHY("도서/티켓/음반"),
  PLANTS("식물"),
  CAR("중고차");

  private final String value;

  Category(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
