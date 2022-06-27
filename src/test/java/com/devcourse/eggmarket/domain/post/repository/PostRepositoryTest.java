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
public class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostAttentionRepository postAttentionRepository;

    private User writer;
    private User notWriter;
    private Post post;

    @BeforeEach
    void setUp() {
        writer = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        notWriter = User.builder()
            .phoneNumber("123456780")
            .nickName("user02")
            .password("User01234*")
            .role("USER")
            .build();

        post = Post.builder()
            .price(1000)
            .content("content01")
            .category(Category.BEAUTY)
            .title("title01")
            .seller(writer)
            .build();

        PostAttention postAttention = new PostAttention(post, notWriter);

        userRepository.save(writer);
        userRepository.save(notWriter);
        postRepository.save(post);
        postAttentionRepository.save(postAttention);
    }

    @AfterEach
    void tearDown() {
        postAttentionRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("조회해 온 포스트 엔티티에 대한 관심개수를 알 수 있다")
    public void attentionCount() {
        Post foundPost = postRepository.findById(this.post.getId()).get();

        Assertions.assertThat(foundPost.getAttentionCount())
            .isEqualTo(1);
    }
}
