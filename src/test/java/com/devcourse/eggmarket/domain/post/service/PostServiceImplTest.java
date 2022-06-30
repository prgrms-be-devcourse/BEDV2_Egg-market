package com.devcourse.eggmarket.domain.post.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.comment.repository.CommentRepository;
import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.exception.NotMatchedSellerException;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostAttentionRepository;
import com.devcourse.eggmarket.domain.post.repository.PostImageRepository;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.stub.UserStub;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    @Spy
    PostServiceImpl postService;

    @Mock
    PostConverter postConverter;

    @Mock
    UserService userService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostAttentionRepository postAttentionRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostImageRepository postImageRepository;

    @Test
    @DisplayName("판매글 생성 테스트")
    void saveTest() {
        PostRequest.Save request = PostStub.writeRequest();
        String loginUser = "test";
        User seller = UserStub.entity();
        Post want = PostStub.entity(seller);

        doReturn(seller)
            .when(userService)
            .getUser(loginUser);
        doReturn(want)
            .when(postConverter)
            .saveToPost(any(PostRequest.Save.class), any(User.class));
        doReturn(want)
            .when(postRepository)
            .save(any(Post.class));

        assertThat(postService.save(request, loginUser).id()).isEqualTo(want.getId());
    }

    @Test
    @DisplayName("판매글 업데이트 테스트")
    void updatePostTest() {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        Long id = 1L;
        String loginUser = "test";
        User seller = UserStub.entity();
        Post post = PostStub.entity(seller);
        Post updatePost = PostStub.updatedEntity(seller);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(anyLong());
        doReturn(seller)
            .when(userService)
            .getUser(anyString());

        assertThat(postService.updatePost(id, request, loginUser).id())
            .isEqualTo(id);
    }

    @Test
    @DisplayName("판매글 업데이트시 전달받은 판매글 ID가 존재하지 않을 경우 예외 발생")
    void updatePostInvalidIdTest() {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        Long invalidId = -1L;
        String loginUser = "test";

        doThrow(new NotExistPostException(NOT_EXIST_POST, invalidId))
            .when(postRepository)
            .findById(invalidId);

        assertThatExceptionOfType(NotExistPostException.class)
            .isThrownBy(() -> postService.updatePost(invalidId, request, loginUser));
    }

    @Test
    @DisplayName("현재 판매글의 판매자 ID와 로그인 사용자의 ID가 다른 경우 예외 발생")
    void updatePostNotMatchedSellerTest() {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        Long id = 1L;
        String loginUser = "test";
        User seller = UserStub.entity();
        User user = UserStub.entity2();
        Post post = PostStub.entity(seller);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(anyLong());
        doReturn(user)
            .when(userService)
            .getUser(anyString());

        assertThatExceptionOfType(NotMatchedSellerException.class)
            .isThrownBy(() -> postService.updatePost(id, request, loginUser));
    }

    @Test
    @DisplayName("판매글 삭제 테스트")
    void deleteTest() {
        Long request = 1L;
        String loginUser = "test";
        User user = UserStub.entity();
        User seller = UserStub.entity();
        Post post = PostStub.entity(seller);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(request);
        doReturn(user)
            .when(userService)
            .getUser(loginUser);
        doNothing()
            .when(postRepository)
            .deleteById(request);

        postService.deleteById(request, loginUser);
        verify(postRepository).findById(request);
        verify(userService).getUser(loginUser);
        verify(postRepository).deleteById(request);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 판매글 삭제 시도시 예외 발생 테스트")
    void deleteInvalidIdTest() {
        Long request = -1L;
        String loginUser = "test";

        doThrow(new NotExistPostException(NOT_EXIST_POST, request))
            .when(postRepository)
            .findById(request);

        assertThatExceptionOfType(NotExistPostException.class)
            .isThrownBy(() -> postService.deleteById(request, loginUser));
    }

    @Test
    @DisplayName("삭제하려는 판매글의 판매자와 로그인 유저가 다를 경우 예외 발생 테스트")
    void deleteNotMatchedTest() {
        Long request = 1L;
        String loginUser = "test";
        User seller = UserStub.entity();
        User user = UserStub.entity2();
        Post post = PostStub.entity(seller);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(anyLong());
        doReturn(user)
            .when(userService)
            .getUser(anyString());

        assertThatExceptionOfType(NotMatchedSellerException.class)
            .isThrownBy(() -> postService.deleteById(request, loginUser));
    }

    @Test
    @DisplayName("판매글 판매상태 업데이트 테스트")
    void updatePurchaseInfoTest() {
        PostRequest.UpdatePurchaseInfo request = PostStub.updatePurchaseInfo();
        Long id = 1L;
        String loginUser = "test";
        User seller = UserStub.entity();
        User buyer = UserStub.entity2();
        Post post = PostStub.entity(seller);
        Post updatePost = PostStub.updatedEntity(seller);
        List<Comment> comments = List.of(new Comment(post, buyer, "test comment"));

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(anyLong());
        doReturn(seller)
            .when(userService)
            .getUser(anyString());
        doReturn(buyer)
            .when(userService)
            .getById(anyLong());
        doReturn(comments)
            .when(commentRepository)
            .findAllByPost(post);

        assertThat(postService.updatePurchaseInfo(id, request, loginUser).id())
            .isEqualTo(id);
    }

    @Test
    @DisplayName("판매글 상태 업데이트시 전달받은 판매글 ID가 존재하지 않을 경우 예외 발생")
    void updatePurchaseInfoInvalidIdTest() {
        PostRequest.UpdatePurchaseInfo request = PostStub.updatePurchaseInfo();
        Long invalidId = -1L;
        String loginUser = "test";

        doThrow(new NotExistPostException(NOT_EXIST_POST, invalidId))
            .when(postRepository)
            .findById(invalidId);

        assertThatExceptionOfType(NotExistPostException.class)
            .isThrownBy(() -> postService.updatePurchaseInfo(invalidId, request, loginUser));
    }

    @Test
    @DisplayName("판매글 상태 업데이트시 판매글의 판매자 ID와 로그인 사용자의 ID가 다른 경우 예외 발생")
    void updatePurchaseInfoNotMatchedSellerTest() {
        PostRequest.UpdatePurchaseInfo request = PostStub.updatePurchaseInfo();
        Long id = 1L;
        String loginUser = "test";
        User seller = UserStub.entity();
        User user = UserStub.entity2();
        Post post = PostStub.entity(seller);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(anyLong());
        doReturn(user)
            .when(userService)
            .getUser(anyString());

        assertThatExceptionOfType(NotMatchedSellerException.class)
            .isThrownBy(() -> postService.updatePurchaseInfo(id, request, loginUser));
    }

    @Test
    @DisplayName("판매글 ID로 판매글 단일조회 테스트")
    void getByIdTest() {
        Long request = 1L;
        String loginUser = "test";
        boolean attention = true;
        User seller = UserStub.entity();
        Post post = PostStub.entity(seller);
        PostResponse.SinglePost response = PostStub.singlePostResponse(post);

        doReturn(Optional.of(post))
            .when(postRepository)
            .findById(request);
        doReturn(seller)
            .when(userService)
            .getUser(loginUser);
        doReturn(Optional.of(attention))
            .when(postAttentionRepository)
            .findByPostIdAndUserId(request, seller.getId());
        doReturn(response)
            .when(postConverter)
            .singlePost(post, attention, null);

        assertThat(postService.getById(request, loginUser))
            .usingRecursiveComparison()
            .isEqualTo(response);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 판매글 단일 조회시 예외 발생")
    void getByIdInvalidTest() {
        Long request = -1L;
        String loginUser = "test";

        doThrow(new NotExistPostException(NOT_EXIST_POST, request))
            .when(postRepository)
            .findById(request);

        assertThatExceptionOfType(NotExistPostException.class)
            .isThrownBy(() -> postService.getById(request, loginUser));
    }

    @Test
    @DisplayName("최신순으로 게시글 조회")
    void getAllLatestTest() {
        Pageable request = PageRequest.of(0, 3);
        PostResponse.Posts response = PostStub.posts();

        doReturn(response)
            .when(postService)
            .getAll(request);

        assertThat(postService.getAll(request))
            .usingRecursiveComparison()
            .isEqualTo(response);
    }

    @Test
    @DisplayName("가격순으로 게시글 조회")
    void getAllPriceTest() {
        Pageable request = PageRequest.of(0, 3, Sort.by("price"));
        PostResponse.Posts response = PostStub.priceSortPosts();

        doReturn(response)
            .when(postService)
            .getAll(request);

        assertThat(postService.getAll(request))
            .usingRecursiveComparison()
            .isEqualTo(response);
    }

    @Test
    @DisplayName("카테고리로 게시글 조회")
    void getAllByCategoryTest() {
        Pageable request = PageRequest.of(0, 3);
        Category category = Category.DIGITAL;
        PostResponse.Posts response = PostStub.posts();

        doReturn(response)
            .when(postService)
            .getAllByCategory(request, category);

        assertThat(postService.getAllByCategory(request, category))
            .usingRecursiveComparison()
            .isEqualTo(response);
    }
}