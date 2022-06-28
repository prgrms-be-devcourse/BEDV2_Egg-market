package com.devcourse.eggmarket.domain.comment.service;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;

public interface CommentService {

    Long write(String userName, Long postId, Save createRequest);
}
