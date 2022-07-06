package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostAttentionRepository postAttentionRepository;

    private User writer;
    private User notWriter;
    private Post likedPost1;
    private Post likedPost2;
    private Post notLikedPost1;
    private Post notLikedPost2;

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

        likedPost1 = Post.builder()
            .price(1000)
            .content("content01")
            .category(Category.BEAUTY)
            .title("title01")
            .seller(writer)
            .build();

        likedPost2 = Post.builder()
            .price(1400)
            .title("title11")
            .content("content12")
            .category(Category.BEAUTY)
            .seller(writer)
            .build();

        notLikedPost1 = Post.builder()
            .price(1400)
            .title("test2")
            .content("content12")
            .category(Category.BEAUTY)
            .seller(writer)
            .build();

        notLikedPost2 = Post.builder()
            .price(1400)
            .title("test1")
            .content("content12")
            .category(Category.BEAUTY)
            .seller(writer)
            .build();

        userRepository.save(writer);
        userRepository.save(notWriter);

        postRepository.save(likedPost1);
        postRepository.save(likedPost2);
        postRepository.save(notLikedPost1);
        postRepository.save(notLikedPost2);

        postAttentionRepository.save(new PostAttention(likedPost1, notWriter));
        postAttentionRepository.save(new PostAttention(likedPost2, notWriter));
    }

    @AfterEach
    void tearDown() {
        postAttentionRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("조회해 온 포스트 엔티티에 대한 관심개수를 알 수 있다")
    void attentionCountTest() {
        Post foundPost = postRepository.findById(this.likedPost1.getId()).get();

        Assertions.assertThat(foundPost.getAttentionCount())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("특정한 사용자가 관심목록에 추가 한 게시글들을 최신순으로 가져올 수 있다")
    void getAttentiontest() {
        Pageable pageable = PageRequest.of(0, 15);

        List<Post> likedList = postRepository.findAllLikedBy(notWriter.getId(), 1L, pageable);

        Assertions.assertThat(likedList.get(0).getId())
            .isEqualTo(3L);
    }

}
