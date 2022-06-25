package com.devcourse.eggmarket.domain.post.converter;

import static org.assertj.core.api.Assertions.*;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostConverterTest {

    private final PostConverter postConverter = new PostConverter();

    @Test
    @DisplayName("판매글 생성 DTO -> Post Entity 변환 테스트")
    void saveToPostTest() {
        PostRequest.Save request = new Save(
            "title",
            "content",
            1000,
            "BEAUTY"
        );
        User seller = User.builder()
            .nickName("test")
            .password("asdfg123!@")
            .phoneNumber("01000000000")
            .role("USER")
            .build();
        Post want = Post.builder()
            .title("title")
            .content("content")
            .price(1000)
            .category(Category.BEAUTY)
            .seller(seller)
            .build();

        Post got = postConverter.saveToPost(request, seller);

        assertThat(got).usingRecursiveComparison().isEqualTo(want);
    }
}