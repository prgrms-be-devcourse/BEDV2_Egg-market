package com.devcourse.eggmarket.domain.post.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import java.util.Collections;
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
            "BEAUTY",
            null
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

    @Test
    @DisplayName("Post , 현재 로그인 한 사용자의 관심목록 추가 여부 정보를 사용하여 PostResponse 를 생성한다")
    public void makeSinglePostResponseTest() {
        User writer = User.builder()
//            .id(1L)
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        Post post = Post.builder()
            .title("title01")
            .content("content01")
            .seller(writer)
            .price(1000)
            .category(Category.BEAUTY)
            .id(1L)
            .build();

        boolean isLoginUserLikePost = false;

        PostResponse.SinglePost expected = new PostResponse.SinglePost(
            post.getId(),
            new UserResponse(writer.getId(), writer.getNickName(), writer.getMannerTemperature(),
                writer.getRole().toString(), writer.getImagePath()),
            post.getPrice(),
            post.getTitle(),
            post.getContent(),
            post.getPostStatus().toString(),
            post.getCategory().toString(),
            post.getCreatedAt(),
            post.getAttentionCount(),
            0, // TODO : Comment 개수
            isLoginUserLikePost,
            Collections.emptyList()
        );

        PostResponse.SinglePost postResponse = postConverter.singlePost(post, isLoginUserLikePost,
            Collections.emptyList());

        assertThat(postResponse).usingRecursiveComparison().isEqualTo(expected);

    }
}