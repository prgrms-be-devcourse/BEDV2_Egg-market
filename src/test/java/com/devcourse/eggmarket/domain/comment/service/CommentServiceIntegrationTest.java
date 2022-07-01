package com.devcourse.eggmarket.domain.comment.service;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Update;
import com.devcourse.eggmarket.domain.comment.exception.CommentNotAllowedPostException;
import com.devcourse.eggmarket.domain.comment.exception.NotExistCommentException;
import com.devcourse.eggmarket.domain.comment.exception.NotWriterException;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Optional;
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
    private Post otherPost;
    private Post post;
    private Comment comment;

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

        otherPost = Post.builder()
            .title("title31")
            .content("content31")
            .seller(postWriter)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        comment = new Comment(post, buyer, "I'll buy it!!!");

        soldPost.updatePurchaseInfo(PostStatus.COMPLETED, buyer);

        userRepository.save(postWriter);
        userRepository.save(user1);
        userRepository.save(buyer);

        postRepository.save(post);
        postRepository.save(soldPost);
        postRepository.save(otherPost);

        commentRepository.save(comment);
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

        boolean isCommentPresent = commentRepository.findById(writtenCommentId).isPresent();

        Assertions.assertThat(isCommentPresent).isTrue();
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

    @Test
    @DisplayName("id 가 commentId 인 댓글이 id 가 postId 인 포스트에 속해 있지 않을 경우 댓글 수정은 실패한다")
    public void updateCommentWithIncorrectPostId() {
        Update updateRequest = new Update("abc");
        User commentWriter = comment.getUser();
        Long postId = otherPost.getId();
        Long commentId = comment.getId();

        Assertions.assertThatThrownBy(() ->
            commentService.update(commentWriter.getNickName(), postId, commentId,
                updateRequest)
        ).isInstanceOf(NotExistCommentException.class);
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 댓글을 수정하려고 할 경우 댓글 권한 에러가 발생한다")
    public void updateFailWithNotCommentWriter() {
        Update updateRequest = new Update("abc");

        Assertions.assertThatThrownBy(() ->
            commentService.update(postWriter.getNickName(), post.getId(), comment.getId(),
                updateRequest)
        ).isInstanceOf(NotWriterException.class);
    }

    @Test
    @DisplayName("id 가 postId 인 포스트가 존재하지 않을 경우 id 가 postId 인 포스트에 속해 있는 커멘트에 대한 댓글 수정은 실패한다")
    public void updateCommentWithNotExistPost() {
        Update updateRequest = new Update("abc");
        User commentWriter = comment.getUser();
        Long postId = 1000L;
        Long commentId = comment.getId();

        Assertions.assertThatThrownBy(() ->
            commentService.update(commentWriter.getNickName(), postId, commentId, updateRequest)
        ).isInstanceOf(NotExistPostException.class);
    }

    @Test
    @DisplayName("id 가 commentId 인 댓글이 id 가 postId 인 포스트에 속해 있는 경우 댓글을 수정한다")
    public void updateCommentWithCorrectPostId() {
        Update updateRequest = new Update("abc");
        User commentWriter = comment.getUser();
        Long postId = post.getId();
        Long commentId = comment.getId();

        Long updatedCommentId = commentService.update(commentWriter.getNickName(), postId,
            commentId,
            updateRequest);

        Assertions.assertThat(updatedCommentId)
            .isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("id 가 commentId 인 댓글이 id 가 postId 인 포스트에 속해 있지 않을 경우 댓글 삭제는 실패한다")
    public void deleteCommentWithIncorrectPostId() {
        User commentWriter = comment.getUser();
        Long postId = otherPost.getId();
        Long commentId = comment.getId();

        Assertions.assertThatThrownBy(() ->
            commentService.delete(commentWriter.getNickName(), postId, commentId)
        ).isInstanceOf(NotExistCommentException.class);
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 사용자가 댓글을 삭제하려고 할 경우 댓글 권한 에러가 발생한다")
    public void deleteFailWithNotCommentWriter() {
        Assertions.assertThatThrownBy(() ->
            commentService.delete(postWriter.getNickName(), post.getId(), comment.getId())
        ).isInstanceOf(NotWriterException.class);
    }

    @Test
    @DisplayName("id 가 postId 인 포스트가 존재하지 않을 경우 id 가 postId 인 포스트에 속해 있는 커멘트에 대한 댓글 삭제는 실패한다")
    public void deleteCommentWithNotExistPost() {
        User commentWriter = comment.getUser();
        Long postId = 1000L;
        Long commentId = comment.getId();

        Assertions.assertThatThrownBy(() ->
            commentService.delete(commentWriter.getNickName(), postId, commentId)
        ).isInstanceOf(NotExistPostException.class);
    }

    @Test
    @DisplayName("id 가 commentId 인 댓글이 id 가 postId 인 포스트에 속해 있는 경우 댓글은 정상적으로 삭제된다")
    public void deleteCommentWithCorrectPostId() {
        User commentWriter = comment.getUser();
        Long postId = post.getId();
        Long commentId = comment.getId();

        commentService.delete(commentWriter.getNickName(), postId,
            commentId);

        Optional<Comment> foundOptionalComment = commentRepository.findByIdAndPostId(commentId,
            postId);

        Assertions.assertThat(foundOptionalComment.isEmpty())
            .isTrue();
    }


}
