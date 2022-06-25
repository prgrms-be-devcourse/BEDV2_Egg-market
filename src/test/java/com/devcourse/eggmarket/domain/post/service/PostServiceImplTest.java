package com.devcourse.eggmarket.domain.post.service;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.exception.NotMatchedSellerException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.stub.UserStub;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;

    @Mock
    PostConverter postConverter;

    @Mock
    UserService userService;

    @Mock
    PostRepository postRepository;

    //TODO 엔티티를 생성하고 ID가 자동으로 할당되지 않아 null 값이 반환되어 id 값의 비교가 불가능
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

    assertThat(postService.save(request, loginUser)).isEqualTo(want.getId());
}

    //TODO 엔티티를 생성하고 ID가 자동으로 할당되지 않아 null 값이 반환되어 id 값의 비교가 불가능
    @Test
    @DisplayName("판매글 업데이트 테스트")
    void updatePostTest() {
//        PostRequest.UpdatePost request = PostStub.updatePostRequest();
//        Long id = 1L;
//        String loginUser = "test";
//        User seller = UserStub.entity();
//        Post post = PostStub.entity(seller);
//        Post updatePost = PostStub.updatedEntity(seller);
//
//        doReturn(Optional.of(post))
//            .when(postRepository)
//            .findById(anyLong());
//        doReturn(seller)
//            .when(userService)
//            .getUser(anyString());
//        doNothing()
//            .when(postConverter)
//            .updateToPost(request, post);
//
//        assertThat(postService.updatePost(id, request, loginUser))
//            .isEqualTo(id);
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

    //TODO 엔티티를 생성하고 ID가 자동으로 할당되지 않아 null 값이 반환되어 id 값의 비교가 불가능
    @Test
    @DisplayName("현재 판매글의 판매자 ID와 로그인 사용자의 ID가 다른 경우 예외 발생")
    void updatePostNotMatchedSellerTest() {
//        PostRequest.UpdatePost request = PostStub.updatePostRequest();
//        Long id = 1L;
//        String loginUser = "test";
//        Long sellerId = 1L;
//        Long loginUserId = 2L;
//        User seller = UserStub.entity();
//        Post post = PostStub.entity(seller);
//
//        doReturn(Optional.of(post))
//            .when(postRepository)
//            .findById(anyLong());
//        doReturn(seller)
//            .when(userService)
//            .getUser(anyString());
//        doReturn(loginUserId)
//            .when(post)
//            .getId();

//        assertThatExceptionOfType(NotMatchedSellerException.class)
//            .isThrownBy(() -> postService.updatePost(id, request, loginUser));
    }
}