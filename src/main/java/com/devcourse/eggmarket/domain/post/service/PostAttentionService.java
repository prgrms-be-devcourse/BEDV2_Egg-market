package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostAttentionCount;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;

public interface PostAttentionService {

    PostAttentionCount toggleAttention(String userName, Long postId);

    Posts getAllLikedBy(String userName);
}
