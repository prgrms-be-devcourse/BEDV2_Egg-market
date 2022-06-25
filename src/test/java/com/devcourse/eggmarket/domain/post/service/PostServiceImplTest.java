package com.devcourse.eggmarket.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.stub.UserStub;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
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

    @Test
    @DisplayName("판매글 업데이트 테스트")
    void updatePostTest() {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        String loginUser = "test";
        User seller = UserStub.entity();
        Post post = PostStub.entity(seller);
        Post updatePost = PostStub.updatedEntity(seller);


    }

    @Test
    @DisplayName("판매글 업데이트시 전달받은 판매글 ID가 존재하지 않을 경우 예외 발생")
    void updatePostInvalidIdTest() {

    }

    @Test
    @DisplayName("현재 판매글의 판매자 ID와 로그인 사용자의 ID가 다른 경우 예뵈 발생")
    void updatePostNotMatchedSellerTest() {

    }
}