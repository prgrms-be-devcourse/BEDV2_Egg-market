package com.devcourse.eggmarket.domain.comment.service;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.exception.CommentNotAllowedPostException;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
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

@SpringBootTest
class CommentServiceIntegrationTest {

    @Autowired
    CommentService commentService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    private User postWriter;
    private User user1;
    private User buyer;
    private Post soldPost;
    private Post post;

    @BeforeEach
    void setUp() {
        postWriter = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        user1 = User.builder()
            .phoneNumber("123456780")
            .nickName("user02")
            .password("User01234*")
            .role("USER")
            .build();

        buyer = User.builder()
            .phoneNumber("123456781")
            .nickName("user03")
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

        soldPost = Post.builder()
            .title("title01")
            .content("content01")
            .seller(postWriter)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        soldPost.updatePurchaseInfo(PostStatus.COMPLETED, buyer);

        userRepository.save(postWriter);
        userRepository.save(user1);
        userRepository.save(buyer);
        postRepository.save(post);
        postRepository.save(soldPost);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("판매글 작성자는 자신의 판매글에 댓글을 달 수 있다")
    public void writeComment() {
        Save createCommentRequest = new Save("my first content");

        Long writtenCommentId = commentService.write(postWriter.getNickName(), post.getId(),
            createCommentRequest);

        List<Comment> postComments = commentRepository.findAllByPost(post);

        Assertions.assertThat(postComments.size())
            .isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 판매글에 대하여 댓글을 달 수 없다")
    public void writeCommentToNotExistingPost() {
        Save createCommentRequest = new Save("my first content");

        Assertions.assertThatThrownBy(() ->
            commentService.write(postWriter.getNickName(), 1000L, createCommentRequest)
        ).isInstanceOf(NotExistPostException.class);
    }

    @Test
    @DisplayName("판매 완료된 글에 대하여 댓글을 달 수 없다")
    public void writeCommentToCompletedPost() {
        Save createCommentRequest = new Save("my first content");

        Assertions.assertThatThrownBy(() ->
            commentService.write(postWriter.getNickName(), soldPost.getId(), createCommentRequest)
        ).isInstanceOf(CommentNotAllowedPostException.class);
    }
}
