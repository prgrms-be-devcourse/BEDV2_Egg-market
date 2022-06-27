package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostAttentionCount;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultPostAttentionService implements PostAttentionService {

    private final PostAttentionRepository postAttentionRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public DefaultPostAttentionService(
        PostAttentionRepository postAttentionRepository,
        PostRepository postRepository,
        UserService userService) {
        this.postAttentionRepository = postAttentionRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    private User getUser(String nickName) {
        return userService.getUser(nickName);
    }

    @Override
    public PostAttentionCount toggleAttention(String userName, Long postId) {
        User loginUser = getUser(userName);

        Post post = postRepository.findById(postId)
            .orElseThrow(EntityNotFoundException::new);

        postAttentionRepository.findByPostIdAndUserId(postId, loginUser.getId())
            .ifPresentOrElse(postAttentionRepository::delete, () ->
                postAttentionRepository.save(new PostAttention(post, loginUser))
            );

        return new PostAttentionCount(
            postAttentionRepository.countPostLikeByPost(postId)
        );
    }
}
