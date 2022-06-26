package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostAttentionCount;

public interface PostAttentionService {

    PostAttentionCount toggleAttention(String userName, Long postId);
}
