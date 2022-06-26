package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostResponse;

public interface PostAttentionService {

    PostResponse.PostLikeCount toggleAttention(String userName, Long postId);
}
