package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceIntegrationTest {
    @Autowired
    public PostAttentionRepository postAttentionRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostAttentionService postAttentionService;

    private User writerLikedOwnPost;
    private Post post;

    @BeforeEach
    void setUp() {
        writerLikedOwnPost = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        post = Post.builder()
            .title("title01")
            .content("content01")
            .seller(writerLikedOwnPost)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        userRepository.save(writerLikedOwnPost);
        postRepository.save(post);
        postAttentionRepository.save(new PostAttention(post, writerLikedOwnPost));
    }

    @AfterEach
    void tearDown() {
        postAttentionRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 사용자가 관심목록 추가 버튼을 누르면 관심상태가 반대 상태로 변경된다")
    public void toggleAttention() {
        postAttentionService.toggleAttention(writerLikedOwnPost.getNickName(), post.getId());

        boolean afterLikeOfMe = postAttentionRepository.findByPostIdAndUserId(post.getId(),
                writerLikedOwnPost.getId())
            .isPresent();

        Assertions.assertThat(afterLikeOfMe).isFalse();
    }
}
