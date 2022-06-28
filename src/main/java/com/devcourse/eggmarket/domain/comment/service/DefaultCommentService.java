package com.devcourse.eggmarket.domain.comment.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;

import com.devcourse.eggmarket.domain.comment.converter.CommentConverter;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.exception.CommentNotAllowedPostException;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class DefaultCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final UserService userService;

    private final CommentConverter converter;

    public DefaultCommentService(
        CommentRepository commentRepository,
        PostRepository postRepository,
        UserService userService,
        CommentConverter converter) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.converter = converter;
    }

    @Override
    public Long write(String userName, Long postId, Save createRequest) {
        User loginUser = getLoginUser(userName);
        Post post = getExistingPost(postId);

        checkNotSoldOutPost(post);

        Comment comment = converter.saveToComment(createRequest, post, loginUser);
        commentRepository.save(comment);

        return comment.getId();
    }

    private Post getExistingPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, postId));
    }

    private void checkNotSoldOutPost(Post post) {
        if (!post.isAbleToDeal()) {
            throw new CommentNotAllowedPostException(post.getId().toString(),
                ErrorCode.SOLD_OUT_POST_NOT_ALLOWED_COMMENT_ERROR);
        }
    }

    private User getLoginUser(String nickName) {
        return userService.getUser(nickName);
    }

}
