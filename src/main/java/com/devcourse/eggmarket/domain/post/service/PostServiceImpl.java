package com.devcourse.eggmarket.domain.post.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_MATCHED_SELLER_POST;

import com.devcourse.eggmarket.domain.model.image.ImageFile;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.PostImageFile;
import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.exception.NotMatchedSellerException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostImage;
import com.devcourse.eggmarket.domain.post.repository.PostImageRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostImageRepository postImageRepository;

    private final UserService userService;

    private final PostConverter postConverter;

    private final ImageUpload imageUpload;

    public PostServiceImpl(PostRepository postRepository,
        PostImageRepository postImageRepository,
        UserService userService,
        PostConverter postConverter,
        ImageUpload imageUpload) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.userService = userService;
        this.postConverter = postConverter;
        this.imageUpload = imageUpload;
    }

    @Transactional
    @Override
    public Long save(PostRequest.Save request, String loginUser) {
        Order order = new Order();
        User seller = userService.getUser(loginUser);

        Post savedPost = postRepository.save(
            postConverter.saveToPost(request, seller)
        );

        Optional.ofNullable(request.images())
            .orElse(Collections.emptyList()).stream()
            .map(img -> PostImageFile.toImage(savedPost.getId(), img, order.order()))
            .forEach(file -> uploadFile(savedPost, file));

        // TODO : 이미지 개수 , HttpRequest 용량 관련 에러 처리

        return savedPost.getId();
    }

    @Transactional
    @Override
    public Long updatePost(Long id, PostRequest.UpdatePost request, String loginUser) {
        Post post = checkPostWriter(id, loginUser);

        postConverter.updateToPost(request, post);

        return post.getId();
    }

    @Transactional
    @Override
    public Long updatePurchaseInfo(Long id, UpdatePurchaseInfo request, String loginUser) {
        Post post = checkPostWriter(id, loginUser);
        User buyer = userService.getUser(request.buyerNickName());
        postConverter.updateToPurchase(request, post, buyer);
        return post.getId();
    }


    @Transactional
    @Override
    public void deleteById(Long id, String loginUser) {
        checkPostWriter(id, loginUser);
        postRepository.deleteById(id);
    }

    @Override
    public PostResponse.SinglePost getById(Long id) {
        return null;
    }

    @Override
    public List<PostResponse.SinglePost> getAll(Pageable pageable) {
        return null;
    }


    @Override
    public List<PostResponse.SinglePost> getAllByCategory(Pageable pageable, String category) {
        return null;
    }

    private String uploadFile(Post post, ImageFile file) {
        return postImageRepository.save(
            PostImage.builder()
                .post(post)
                .imagePath(imageUpload.upload(file))
                .build()
        ).getImagePath();
    }

    private Post checkPostWriter(Long postId, String loginUser) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, postId));
        Long loginUserId = userService.getUser(loginUser).getId();
        Long sellerId = post.getSeller().getId();
        if (!(sellerId.equals(loginUserId))) {
            throw new NotMatchedSellerException(NOT_MATCHED_SELLER_POST, sellerId, loginUserId);
        }

        return post;
    }

    private static class Order {

        private final Sequence order;

        private Order(Sequence order) {
            this.order = order;
        }

        public Order() {
            this(new Sequence());
        }

        public int order() {
            int pre = this.order.order;

            this.order.increase();

            return pre;
        }

        private static class Sequence {

            private int order = 0;

            public void increase() {
                this.order++;
            }
        }
    }
}
