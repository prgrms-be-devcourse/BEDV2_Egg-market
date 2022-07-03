package com.devcourse.eggmarket.domain.comment.service;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.Comments;

public interface CommentService {

    Long write(String userName, Long postId, Save createRequest);

    Long update(String userName, Long postId, Long commentId, CommentRequest.Update updateRequest);

    Comments getAllComments(String userName, Long postId, Long lastId);

    void delete(String userName, Long postId, Long commentId);
}
