package com.devcourse.eggmarket.domain.post.exception;

public class PostExceptionMessage {

    private PostExceptionMessage() {
    }

    public static final String ALREADY_COMPLETED = "이미 판매 완료된 게시글입니다";

    public static final String NOT_EXIST_BUYER = "구매자를 입력해주세요";

    public static final String NOT_BLANK_TITLE = "제목을 입력 해주세요";

    public static final String NOT_VALID_RANGE_TITLE = "제목은 1 ~ 255자 이어야 합니다";

    public static final String NOT_BLANK_CONTENT = "본문을 입력 해주세요";

    public static final String NOT_VALID_RANGE_CONTENT = "본문은 1 ~ 1024자 이어야 합니다";

    public static final String NOT_BLANK_CATEGORY = "카테고리를 선택 해주세요";

    public static final String NOT_NEGATIVE_PRICE = "가격은 음수일 수 없습니다";

    public static final String NOT_NEGATIVE_ID = "아이디는 양수여야 합니다.";

    public static final String NOT_VALID_RANGE_CATEGORY = "카테고리는 1 ~ 20자 이어야 합니다";

    public static final String NOT_BLANK_POST_STATUS = "판매 상태를 선택 해주세요";

    public static final String NOT_VALID_RANGE_POST_STATUS = "판매 상태는 1 ~ 20자 이어야 합니다";

    public static final String NOT_NULL_USER = "유저 아이디를 입력해주세요";

    public static final String NOT_VALID_RANGE_USER = "닉네임은 3 ~ 12자 이어야 합니다";

    public static final String NOT_VALID_CATEGORY = "잘못 된 카테고리 입니다";

    public static final String NOT_VALID_POST_STATUS = "잘못 된 판매 상태 입니다";

    public static final String NOT_EXIST_POST = "존재하지 않는 판매글 입니다. 조회한 판매글 번호 : ";

    public static final String NOT_MATCHED_SELLER_POST = "판매자와 로그인 유저가 다릅니다. 판매자, 로그인 유저 : ";

    public static final String FILE_NUMBER_LIMIT_EXCEED = "첨부 파일은 10개를 넘을 수 없습니다";
}
