package com.devcourse.eggmarket.domain.comment.converter;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.CommentsElement;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public Comment saveToComment(CommentRequest.Save saveRequest, Post post, User writer) {
        return Comment.builder()
            .content(saveRequest.content())
            .post(post)
            .user(writer)
            .build();
    }

    public CommentsElement commentToCommentsElement(Comment comment) {
        User commentWriter = comment.getUser();

        return new CommentsElement(comment.getId(),
            new UserResponse.Basic(commentWriter.getId(),
                commentWriter.getNickName(),
                commentWriter.getMannerTemperature(),
                commentWriter.getRole().name(),
                commentWriter.getImagePath()),
            comment.getContent(),
            comment.getCreatedAt());
    }

    public Comments commentListToComments(Collection<Comment> commentCollections) {
        return new Comments(commentCollections.stream()
            .map(this::commentToCommentsElement)
            .collect(Collectors.toList())
        );
    }
}
