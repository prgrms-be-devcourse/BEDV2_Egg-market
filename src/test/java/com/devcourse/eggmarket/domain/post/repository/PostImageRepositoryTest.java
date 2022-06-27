package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostImage;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
class PostImageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    private User writer;
    private Post post;

    @BeforeEach
    void setUp() {
        writer = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        post = Post.builder()
            .title("title01")
            .content("content01")
            .seller(writer)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        userRepository.save(writer);
        postRepository.save(post);
    }

    @Test
    @DisplayName("인자로 전달받은 판매글에 대한 이미지 경로들을 가져올 수 있다")
    public void findByPost() {
        // when
        String[] givenImgPathArray = new String[]{"/src/post/1_1.png", "/src/post/1_2.png"};

        List<PostImage> postImgPaths = Arrays.stream(givenImgPathArray)
            .map(path -> postImageRepository.save(
                new PostImage(post, path)
            )).toList();

        // given
        List<PostImage> foundImgPaths = postImageRepository.findByPost(post);

        // then
        Assertions.assertThat(foundImgPaths.size())
            .isEqualTo(givenImgPathArray.length);
    }
}