package com.devcourse.eggmarket.domain.post.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_MATCHED_SELLER_POST;

import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.exception.NotMatchedSellerException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final PostConverter postConverter;

    public PostServiceImpl(PostRepository postRepository,
        UserService userService,
        PostConverter postConverter) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postConverter = postConverter;
    }

    @Transactional
    @Override
    public Long save(PostRequest.Save request, String loginUser) {
        User seller = userService.getUser(loginUser);
        Post post = postConverter.saveToPost(request, seller);

        return postRepository.save(post).getId();
    }

    @Transactional
    @Override
    public Long updatePost(Long id, PostRequest.UpdatePost request, String loginUser) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, id));
        Long loginUserId = userService.getUser(loginUser).getId();
        Long sellerId = post.getSeller().getId();
        if (!(sellerId.equals(loginUserId))) {
            throw new NotMatchedSellerException(NOT_MATCHED_SELLER_POST, sellerId, loginUserId);
        }
        postConverter.updateToPost(request, post);
        return post.getId();
    }

    @Transactional
    @Override
    public Long updatePurchaseInfo(PostRequest.UpdatePurchaseInfo request) {
        return null;
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public PostResponse getById(Long id) {
        return null;
    }

    @Override
    public List<PostResponse> getAll(Pageable pageable) {
        return null;
    }


    @Override
    public List<PostResponse> getAllByCategory(Pageable pageable, String category) {
        return null;
    }
}
