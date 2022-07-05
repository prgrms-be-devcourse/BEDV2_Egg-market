package com.devcourse.eggmarket.domain.comment.converter;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.CommentsElement;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.model.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentConverterTest {

    private final CommentConverter converter = new CommentConverter();

    private User seller;
    private Post post;
    private Comment postComment;

    @BeforeEach
    void setUp() {
        seller = User.builder() // FIXME: id 세팅 불가능한 상황
            .phoneNumber("010123456")
            .nickName("abc")
            .password("abcDEF12*")
            .role(UserRole.USER.name())
            .build();

        post = Post.builder()
            .content("content01")
            .title("title01")
            .price(1000)
            .category(Category.BEAUTY)
            .seller(seller)
            .build();

        postComment = Comment.builder()
            .user(seller)
            .post(post)
            .content("I'm first comment")
            .build();

    }

    @Test
    @DisplayName("커멘트 생성 요청 객체로부터 댓글 엔티티를 생성한다")
    void saveToComment() {
        Comment expectedComment = Comment.builder()
            .content("content")
            .post(post)
            .user(seller)
            .build();

        Save commentSaveRequest = new Save("content");

        Comment convertedComment = converter.saveToComment(commentSaveRequest, post, seller);

        Assertions.assertThat(convertedComment)
            .usingRecursiveComparison()
            .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("커멘트 엔티티로부터 CommentsElement 를 생성한다")
    void commentToCommentsElement() {
        User commentWriter = postComment.getUser();

        CommentsElement expectedDto = new CommentsElement(
            postComment.getId(),
            new UserResponse.Basic(commentWriter.getId(),
                commentWriter.getNickName(),
                commentWriter.getMannerTemperature(),
                commentWriter.getRole().name(),
                commentWriter.getImagePath()),
            postComment.getContent(),
            postComment.getCreatedAt()
        );

        CommentsElement commentsElement = converter.commentToCommentsElement(postComment);

        Assertions.assertThat(commentsElement)
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("커멘트 엔티티 목록으로부터 Comments dto 를 생성한다")
    void commentListToComments() {
        User commentWriter = seller;
        List<Comment> postComments = IntStream.range(0, 10)
            .mapToObj(numb -> Comment.builder()
                .content("content" + numb)
                .post(post)
                .user(seller)
                .build())
            .collect(Collectors.toList());

        List<CommentsElement> expectedComments =
            postComments.stream()
                .map(comment -> new CommentsElement(comment.getId(),
                    new UserResponse.Basic(
                        commentWriter.getId(),
                        commentWriter.getNickName(),
                        commentWriter.getMannerTemperature(),
                        commentWriter.getRole().name(),
                        commentWriter.getImagePath()),
                    comment.getContent(),
                    comment.getCreatedAt()))
                .collect(Collectors.toList());

        Comments comments = converter.commentListToComments(postComments);

        Assertions.assertThat(comments.comments())
            .usingRecursiveComparison()
            .isEqualTo(expectedComments);
    }
}