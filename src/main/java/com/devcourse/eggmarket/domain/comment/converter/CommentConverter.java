package com.devcourse.eggmarket.domain.comment.converter;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.model.User;
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
}
