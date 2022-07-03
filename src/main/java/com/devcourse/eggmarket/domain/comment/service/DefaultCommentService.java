package com.devcourse.eggmarket.domain.comment.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;

import com.devcourse.eggmarket.domain.comment.converter.CommentConverter;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Update;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.eggmarket.domain.comment.exception.CommentNotAllowedPostException;
import com.devcourse.eggmarket.domain.comment.exception.NotExistCommentException;
import com.devcourse.eggmarket.domain.comment.exception.NotWriterException;
import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DefaultCommentService implements CommentService {

    private static final int COMMENT_PAGING_SIZE = 15;
    private static final int COMMENT_PAGE = 0;

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
    @Transactional
    public Long write(String userName, Long postId, Save createRequest) {
        User loginUser = getLoginUser(userName);
        Post post = getExistingPost(postId);

        checkNotSoldOutPost(post);

        Comment comment = converter.saveToComment(createRequest, post, loginUser);
        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    @Transactional
    public Long update(String userName, Long postId, Long commentId, Update updateRequest) {
        User loginUser = getLoginUser(userName);
        Post post = getExistingPost(postId);

        Comment foundComment = getExistingComment(commentId, post);
        checkCommentWriter(loginUser, foundComment);

        return commentId;
    }

    @Override
    public Comments getAllComments(String userName, Long postId, Long lastId) {
        User loginUser = getLoginUser(
            userName); // FIXME : 이를 확인한다는 것은, 인가된 사용자만이 댓글 목록을 조회할 수 있음을 의미
        Post post = getExistingPost(postId);

        Pageable pageRequest = PageRequest.of(COMMENT_PAGE, COMMENT_PAGING_SIZE);

        List<Comment> comments = Optional.ofNullable(lastId)
            .map(id ->
                commentRepository.findAllByPostAndIdGreaterThan(post, id, pageRequest))
            .orElse(commentRepository.findAllByPost(post, pageRequest));

        return converter.commentListToComments(comments);
    }

    @Override
    @Transactional
    public void delete(String userName, Long postId, Long commentId) {
        User loginUser = getLoginUser(userName);
        Post post = getExistingPost(postId);

        Comment comment = getExistingComment(commentId, post);

        checkCommentWriter(loginUser, comment);

        commentRepository.delete(comment);
    }

    private Post getExistingPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, postId));
    }

    private Comment getExistingComment(Long commentId, Post post) {
        return commentRepository.findByIdAndPostId(commentId, post.getId())
            .orElseThrow(() ->
                new NotExistCommentException("해당 포스트에 존재하지 않는 댓글입니다"));
    }

    private void checkCommentWriter(User loginUser, Comment comment) {
        if (!comment.isWriter(loginUser)) {
            throw new NotWriterException(ErrorCode.NOT_ALLOWED_USER);
        }
    }

    private void checkNotSoldOutPost(Post post) {
        if (!post.isAbleToDeal()) {
            throw new CommentNotAllowedPostException(
                ErrorCode.SOLD_OUT_POST_NOT_ALLOWED_COMMENT_ERROR);
        }
    }

    private User getLoginUser(String nickName) {
        return userService.getUser(nickName);
    }

}
