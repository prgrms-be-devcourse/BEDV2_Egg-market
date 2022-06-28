package com.devcourse.eggmarket.domain.comment.repository;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User postWriter;
    private Post post;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        postWriter = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        post = Post.builder()
            .title("title01")
            .content("content01")
            .seller(postWriter)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        userRepository.save(postWriter);
        postRepository.save(post);
        commentRepository.save(
            new Comment(post, postWriter, "I'm writing comment to my own post!"));
        commentRepository.save(
            new Comment(post, postWriter, "I'm writing comment to my own post!"));
    }

    @Test
    @DisplayName("해당 판매글의 댓글들을 가져올 수 있다")
    public void findAllByPost() {
        List<Comment> allComments = commentRepository.findAllByPost(post);

        Assertions.assertThat(allComments.size())
            .isEqualTo(2);
    }
}