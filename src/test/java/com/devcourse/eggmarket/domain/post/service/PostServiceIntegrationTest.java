package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.SinglePost;
import com.devcourse.eggmarket.domain.post.exception.InvalidBuyerException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostAttention;
import com.devcourse.eggmarket.domain.post.model.PostImage;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostImageRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.stub.ImageStub;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PostServiceIntegrationTest {

    @Autowired
    private PostAttentionRepository postAttentionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private PostAttentionService postAttentionService;

    @Autowired
    private PostService postService;

    private User writerLikedOwnPost;
    private User notYetLikedUser;
    private User notCommentWriter;
    private User commentWriter;
    private Post likedPost1;
    private Post likedPost2;
    private Post notLikedPost1;
    private Post notLikedPost2;

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

        notLikedPost1 = Post.builder()
            .title("test1")
            .content("content")
            .price(1000)
            .category(Category.BEAUTY)
            .seller(writerLikedOwnPost)
            .build();
        notLikedPost2 = Post.builder()
            .title("test2")
            .content("content")
            .price(2500)
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
        postRepository.save(notLikedPost1);
        postRepository.save(notLikedPost2);

        postAttentionRepository.save(new PostAttention(likedPost1, writerLikedOwnPost));
        postAttentionRepository.save(new PostAttention(likedPost2, writerLikedOwnPost));

        postImageRepository.save(PostImage.builder()
            .post(likedPost1)
            .imagePath(ImageStub.uploadImage1(likedPost1.getId()).pathTobeStored(""))
            .build()
        );
        postImageRepository.save(PostImage.builder()
            .post(likedPost1)
            .imagePath(ImageStub.uploadImage2(likedPost1.getId()).pathTobeStored(""))
            .build()
        );
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
    @DisplayName("??????????????? ???????????? ????????? ???????????? ???????????? ?????? ????????? ????????? ?????????????????? ????????????")
    void toggleAttentionToDisable() {
        postAttentionService.toggleAttention(writerLikedOwnPost.getNickName(), likedPost1.getId());

        boolean afterLikeOfMe = postAttentionRepository.findByPostIdAndUserId(likedPost1.getId(),
                writerLikedOwnPost.getId())
            .isPresent();

        Assertions.assertThat(afterLikeOfMe).isFalse();
    }

    @Test
    @DisplayName("??????????????? ????????? ??? ?????? ????????? ???????????? ???????????? ?????? ????????? ????????? ??????????????? ????????????")
    void toggleAttentionToEnable() {
        postAttentionService.toggleAttention(notYetLikedUser.getNickName(), likedPost1.getId());

        boolean afterLikeOfMe = postAttentionRepository.findByPostIdAndUserId(likedPost1.getId(),
                notYetLikedUser.getId())
            .isPresent();

        Assertions.assertThat(afterLikeOfMe).isTrue();
    }

    @Test
    @DisplayName("????????? ???????????? ?????????????????? lastId ???????????? ??????????????? ????????????")
    void getAllLikedPosts() {
        Long lastId = 1L;

        Posts allLikedPosts = postService.getAllLikedBy(
            writerLikedOwnPost.getNickName(), lastId);

        Assertions.assertThat(allLikedPosts.posts().size())
            .isEqualTo(2L);
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ???????????? ????????? ???????????? ???????????? ????????? ??? ??????")
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
    @DisplayName("????????? ???????????? ???????????? ???????????? ????????? ??? ??????")
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
    @DisplayName("???????????? ?????? ?????? ???????????? ???????????? ???????????? ????????? ??? ??????")
    void updatePostInfoSuccess() {
        UpdatePurchaseInfo updatePurchaseRequest = new UpdatePurchaseInfo(
            PostStatus.COMPLETED.name(),
            commentWriter.getId());

        Long response = postService.updatePurchaseInfo(
            likedPost1.getId(),
            updatePurchaseRequest,
            likedPost1.getSeller().getNickName()
        );

        Assertions.assertThat(response).isEqualTo(likedPost1.getId());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void getByIdTest() {
        SinglePost got = postService.getById(likedPost1.getId(), writerLikedOwnPost.getNickName());
        SinglePost want = PostStub.singlePostResponse(likedPost1);

        Assertions.assertThat(got.id()).isEqualTo(want.id());
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ?????????")
    void searchTest() {
        Pageable pageable = PageRequest.of(0, 2);
        String word = "test";
        PostResponse.Posts got = postService.search(pageable, word);
        PostResponse.Posts want = PostStub.searchPosts(notLikedPost1.getId(), notLikedPost2.getId());

        Assertions.assertThat(got)
            .usingRecursiveComparison()
            .ignoringFields("posts.createdAt")
            .isEqualTo(want);
    }
}
