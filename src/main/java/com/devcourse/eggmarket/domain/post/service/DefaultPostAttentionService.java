package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostAttentionCount;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostsElement;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.post.model.PostImage;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostImageRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultPostAttentionService implements PostAttentionService {

    private final PostAttentionRepository postAttentionRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostConverter postConverter;
    private final UserService userService;

    public DefaultPostAttentionService(
        PostAttentionRepository postAttentionRepository,
        PostRepository postRepository,
        PostConverter postConverter,
        PostImageRepository postImageRepository,
        UserService userService) {
        this.postAttentionRepository = postAttentionRepository;
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.postImageRepository = postImageRepository;
        this.userService = userService;
    }

    @Override
    public PostAttentionCount toggleAttention(String userName, Long postId) {
        User loginUser = getUser(userName);

        Post post = postRepository.findById(postId)
            .orElseThrow(EntityNotFoundException::new);

        postAttentionRepository.findByPostIdAndUserId(postId, loginUser.getId())
            .ifPresentOrElse(
                postAttentionRepository::delete, () ->
                    postAttentionRepository.save(new PostAttention(post, loginUser))
            );

        return new PostAttentionCount(
            postAttentionRepository.countPostLikeByPost(postId)
        );
    }

    @Override
    public Posts getAllLikedBy(String userName) {
        return new Posts(
            postRepository.findAllLikedBy(getUserId(userName)).stream()
                .map(this::thumbnailAddedPostResponse)
                .collect(Collectors.toList()));
    }

    private PostsElement thumbnailAddedPostResponse(Post post) {
        String path = postImageRepository.findAllByPost(post).stream()
            .findFirst()
            .map(PostImage::getImagePath)
            .orElse(null);

        return postConverter.postsElement(post, path);
    }

    private User getUser(String nickName) {
        return userService.getUser(nickName);
    }

    private Long getUserId(String nickName) {
        return this.getUser(nickName).getId();
    }
}
