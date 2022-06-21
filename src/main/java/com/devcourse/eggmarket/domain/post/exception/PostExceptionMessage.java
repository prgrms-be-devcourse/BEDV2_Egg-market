package com.devcourse.eggmarket.domain.post.exception;

public class PostExceptionMessage {

  private PostExceptionMessage() {
  }

  public static final String ALREADY_REGISTERED_USER = "이미 구매자가 등록된 게시글입니다.\n현재 등록된 구매자 닉네임 : ";

  public static final String NOT_BLANK_TITLE = "제목을 입력 해주세요";

  public static final String NOT_VALID_RANGE_TITLE = "제목은 1 ~ 255자 이어야 합니다";

  public static final String NOT_BLANK_CONTENT = "본문을 입력 해주세요";

  public static final String NOT_VALID_RANGE_CONTENT = "본문은 1 ~ 1024자 이어야 합니다";

  public static final String NOT_BLANK_CATEGORY = "카테고리를 선택 해주세요";

  public static final String NOT_NEGATIVE_PRICE = "가격은 음수일 수 없습니다";

  public static final String NOT_VALID_RANGE_CATEGORY = "카테고리는 1 ~ 20자 이어야 합니다";

  public static final String NOT_BLANK_POST_STATUS = "판매 상태를 선택 해주세요";

  public static final String NOT_VALID_RANGE_POST_STATUS = "판매 상태는 1 ~ 20자 이어야 합니다";

  public static final String NOT_NULL_USER = "유저 닉네임을 입력 해주세요";

  public static final String NOT_VALID_RANGE_USER = "닉네임은 3 ~ 12자 이어야 합니다";
}
