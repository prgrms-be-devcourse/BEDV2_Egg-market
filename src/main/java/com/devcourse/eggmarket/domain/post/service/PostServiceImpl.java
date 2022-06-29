package com.devcourse.eggmarket.domain.post.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_MATCHED_SELLER_POST;

import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.model.image.ImageFile;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.PostImageFile;
import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostsElement;
import com.devcourse.eggmarket.domain.post.exception.InvalidBuyerException;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.exception.NotMatchedSellerException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostImage;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostImageRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;
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

    private final CommentRepository commentRepository;

    private final UserService userService;

    private final PostConverter postConverter;

    private final ImageUpload imageUpload;

    private final PostAttentionRepository postAttentionRepository;

    public PostServiceImpl(PostRepository postRepository,
        PostImageRepository postImageRepository,
        CommentRepository commentRepository,
        UserService userService,
        PostConverter postConverter,
        ImageUpload imageUpload,
        PostAttentionRepository postAttentionRepository) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postConverter = postConverter;
        this.imageUpload = imageUpload;
        this.postAttentionRepository = postAttentionRepository;
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
        User buyer = userService.getById(request.buyerId());

        checkAllowedBuyer(buyer, post);

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
    public PostResponse.SinglePost getById(Long id, String loginUser) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, id));
        User user = userService.getUser(loginUser);
        boolean attention = postAttentionRepository.findByPostIdAndUserId(id, user.getId())
            .isPresent();

        return postConverter.singlePost(post, attention, null);
    }

    @Override
    public PostResponse.Posts getAll(Pageable pageable) {
        return new Posts(postRepository.findAll(pageable)
            .map(this::postResponseAddThumbnail)
            .getContent()
        );
    }

    @Override
    public PostResponse.Posts getAllByCategory(Pageable pageable, Category category) {
        return new Posts(postRepository.findAllByCategory(pageable, category)
            .map(this::postResponseAddThumbnail)
            .getContent()
        );
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

    private PostsElement postResponseAddThumbnail(Post post) {
        String path = null;

        List<PostImage> images = postImageRepository.findByPost(post);
        if (!images.isEmpty()) {
            path = images.get(0).getImagePath();
        }
        return postConverter.postsElement(post, path);
    }

    private void checkAllowedBuyer(User buyer, Post post) {
        checkSellerIsNotBuyer(buyer, post);

        commentRepository.findAllByPost(post).stream()
            .filter(comment -> comment.getUser().isSameUser(buyer))
            .findAny()
            .orElseThrow(() -> new InvalidBuyerException(
                post.getId() + "판매글의 구매자로 등록할 수 없는 사용자입니다 : " + buyer.getId(),
                ErrorCode.NOT_ALLOWED_BUYER));
    }

    private void checkSellerIsNotBuyer(User buyer, Post post) {
        if (post.getSeller().isSameUser(buyer)) {
            throw new InvalidBuyerException(
                post.getId() + "판매글의 구매자로 등록할 수 없는 사용자입니다 : " + buyer.getId(),
                ErrorCode.NOT_ALLOWED_BUYER);
        }
    }

}
