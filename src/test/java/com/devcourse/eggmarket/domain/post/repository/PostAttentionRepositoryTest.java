package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
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
class PostAttentionRepositoryTest {

    @Autowired
    public PostAttentionRepository postAttentionRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PostRepository postRepository;

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
    @DisplayName("Post 와 로그인 사용자 정보를 통해 해당 사용자가 해당 게시글에 관심목록으로 추가했는지 여부를 알 수 있다")
    public void findPostAttentionByPostAndUser() {
        boolean present = postAttentionRepository.findByPostIdAndUserId(post.getId(),
                writerLikedOwnPost.getId())
            .isPresent();

        Assertions.assertThat(present).isTrue();
    }
}