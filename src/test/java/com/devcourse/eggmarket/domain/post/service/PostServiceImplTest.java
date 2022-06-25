package com.devcourse.eggmarket.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import com.devcourse.eggmarket.domain.post.converter.PostConverter;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
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
        PostRequest.Save request = new Save("title", "content", 1000, "BEAUTY");
        String loginUser = "test";
        User seller = User.builder()
            .nickName("test")
            .password("asdfg123!@")
            .phoneNumber("01000000000")
            .role("USER")
            .build();
        Post want = Post.builder()
            .title("title")
            .content("content")
            .price(1000)
            .category(Category.BEAUTY)
            .seller(seller)
            .build();

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
}