package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.exception.InvalidBuyerException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import com.devcourse.eggmarket.global.common.SuccessResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceIntegrationTest {

    @Autowired
    public PostAttentionRepository postAttentionRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public CommentRepository commentRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostAttentionService postAttentionService;

    @Autowired
    public PostService postService;

    private User writerLikedOwnPost;
    private User notYetLikedUser;
    private User notCommentWriter;
    private User commentWriter;
    private Post likedPost1;
    private Post likedPost2;

    @BeforeEach
    void setUp() {
        writerLikedOwnPost = User.builder()
            .phoneNumber("123456789")
            .nickName("user01")
            .password("User01234*")
            .role("USER")
            .build();

        notYetLikedUser = User.builder()
            .phoneNumber("123456780")
            .nickName("user02")
            .password("User01234*")
            .role("USER")
            .build();

        notCommentWriter = User.builder()
            .phoneNumber("123456711")
            .nickName("user03")
            .password("User01234*")
            .role("USER")
            .build();

        commentWriter = User.builder()
            .phoneNumber("123456734")
            .nickName("user04")
            .password("User01234*")
            .role("USER")
            .build();

        likedPost1 = Post.builder()
            .title("title01")
            .content("content01")
            .seller(writerLikedOwnPost)
            .price(1000)
            .category(Category.BEAUTY)
            .build();

        likedPost2 = Post.builder()
            .price(1400)
            .title("title11")
            .content("content12")
            .category(Category.BEAUTY)
            .seller(writerLikedOwnPost)
            .build();

        Comment comment1 = new Comment(likedPost1, commentWriter, "I want to buy your stuff");

        userRepository.save(writerLikedOwnPost);
        userRepository.save(notYetLikedUser);
        userRepository.save(notCommentWriter);
        userRepository.save(commentWriter);

        postRepository.save(likedPost1);
        postRepository.save(likedPost2);

        postAttentionRepository.save(new PostAttention(likedPost1, writerLikedOwnPost));
        postAttentionRepository.save(new PostAttention(likedPost2, writerLikedOwnPost));

        commentRepository.save(comment1);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        postAttentionRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("관심목록에 추가했던 로그인 사용자가 관심목록 추가 버튼을 누르면 관심목록에서 제거한다")
    void toggleAttentionToDisable() {
        postAttentionService.toggleAttention(writerLikedOwnPost.getNickName(), likedPost1.getId());

        boolean afterLikeOfMe = postAttentionRepository.findByPostIdAndUserId(likedPost1.getId(),
                writerLikedOwnPost.getId())
            .isPresent();

        Assertions.assertThat(afterLikeOfMe).isFalse();
    }

    @Test
    @DisplayName("관심목록에 추가한 적 없던 로그인 사용자가 관심목록 추가 버튼을 누르면 관심목록에 추가한다")
    void toggleAttentionToEnable() {
        postAttentionService.toggleAttention(notYetLikedUser.getNickName(), likedPost1.getId());

        boolean afterLikeOfMe = postAttentionRepository.findByPostIdAndUserId(likedPost1.getId(),
                notYetLikedUser.getId())
            .isPresent();

        Assertions.assertThat(afterLikeOfMe).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자는 자신의 관심목록을 확인한다")
    void getAllLikedPosts() {
        Posts allLikedPosts = postAttentionService.getAllLikedBy(
            writerLikedOwnPost.getNickName());

        Assertions.assertThat(allLikedPosts.posts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("판매글에 대한 댓글 작성자가 아니면 판매글의 구매자로 등록될 수 없다")
    void updatePostInfoFail() {
        UpdatePurchaseInfo updatePurchaseRequest = new UpdatePurchaseInfo(
            PostStatus.COMPLETED.name(),
            notCommentWriter.getId());

        Assertions.assertThatThrownBy(() ->
            postService.updatePurchaseInfo(
                likedPost1.getId(),
                updatePurchaseRequest,
                likedPost1.getSeller().getNickName())
        ).isInstanceOf(InvalidBuyerException.class);
    }

    @Test
    @DisplayName("판매글 작성자는 판매글의 구매자로 등록될 수 없다")
    void updatePostInfoFailWithSeller() {
        UpdatePurchaseInfo updatePurchaseRequest = new UpdatePurchaseInfo(
            PostStatus.COMPLETED.name(),
            writerLikedOwnPost.getId());

        Assertions.assertThatThrownBy(() ->
            postService.updatePurchaseInfo(
                likedPost1.getId(),
                updatePurchaseRequest,
                likedPost1.getSeller().getNickName())
        ).isInstanceOf(InvalidBuyerException.class);
    }

    @Test
    @DisplayName("판매글에 대한 댓글 작성자는 판매글의 구매자로 등록할 수 있다")
    void updatePostInfoSuccess() {
        UpdatePurchaseInfo updatePurchaseRequest = new UpdatePurchaseInfo(
            PostStatus.COMPLETED.name(),
            commentWriter.getId());

        SuccessResponse<Long> response = new SuccessResponse<>(
            postService.updatePurchaseInfo(
                likedPost1.getId(),
                updatePurchaseRequest,
                likedPost1.getSeller().getNickName()
            )
        );

        Assertions.assertThat(response.getData()).isEqualTo(likedPost1.getId());
    }
}
