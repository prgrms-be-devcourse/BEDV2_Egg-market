package com.devcourse.eggmarket.domain.post.api;

import static com.devcourse.eggmarket.domain.post.exception.PostExceptionMessage.NOT_EXIST_POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.exception.NotExistPostException;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.repository.PostRepository;
import com.devcourse.eggmarket.domain.stub.ImageStub;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.stub.UserStub;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PostRepository postRepository;

    Long presentId = 0L;
    Post lastPost;

    @BeforeAll
    void userSignUp() throws Exception {
        UserRequest.Save sellerSignupRequest = UserStub.saveRequest();
        mockMvc.perform(
            multipart("/signup")
                .param("nickName", sellerSignupRequest.nickName())
                .param("phoneNumber", sellerSignupRequest.phoneNumber())
                .param("password", sellerSignupRequest.password())
                .param("isAdmin", sellerSignupRequest.isAdmin() ? "true" : "false")
        );
        UserRequest.Save buyerSignupRequest = UserStub.saveRequest2();
        mockMvc.perform(
            multipart("/signup")
                .param("nickName", buyerSignupRequest.nickName())
                .param("phoneNumber", buyerSignupRequest.phoneNumber())
                .param("password", buyerSignupRequest.password())
                .param("isAdmin", buyerSignupRequest.isAdmin() ? "true" : "false")
        );
    }

    @BeforeEach
    void setPosts() throws Exception {
        List<MockMultipartFile> images = ImageStub.images();
        mockMvc.perform(
            multipart("/posts")
                .file(images.get(0))
                .file(images.get(1))
                .file(images.get(2))
                .param("title", "title1")
                .param("content", "content1")
                .param("price", "8700")
                .param("category", "BEAUTY")

        );
        mockMvc.perform(
            multipart("/posts")
                .file(images.get(1))
                .param("title", "title11")
                .param("content", "title11")
                .param("price", "200000")
                .param("category", "DIGITAL")
        );
        mockMvc.perform(
            multipart("/posts")
                .param("title", "title2")
                .param("content", "content2")
                .param("price", "3000")
                .param("category", "BEAUTY")
        );
        presentId += 3;
    }

    @AfterEach
    void deletePosts() {
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("게시글 작성 테스트")
    void writeTest() throws Exception {
        PostRequest.Save request = PostStub.writeRequest();
        Long want = ++presentId;
        List<MockMultipartFile> images = ImageStub.images();

        ResultActions resultActions = mockMvc.perform(
            multipart("/posts")
                .file(images.get(0))
                .file(images.get(1))
                .file(images.get(2))
                .param("title", request.title())
                .param("content", request.content())
                .param("price", String.valueOf(request.price()))
                .param("category", request.category())
        );

        resultActions.andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").value(want));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 수정 테스트")
    void updatePostTest() throws Exception {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        Long want = presentId;

        ResultActions resultActions = mockMvc.perform(
            patch("/posts/{id}", want)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(want));
    }

    @Disabled
    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 상태변경 테스트")
    void updatePurchaseTest() throws Exception {
        PostRequest.UpdatePurchaseInfo request = PostStub.updatePurchaseInfo();
        Long want = presentId;

        registerCommand(want);
        ResultActions resultActions = mockMvc.perform(
            patch("/posts/{id}/purchase", want)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value(want));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 삭제 테스트")
    void deleteTest() throws Exception {
        Long request = presentId;

        ResultActions resultActions = mockMvc.perform(
            delete("/posts/{id}", request)
        );

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 단일 조회 테스트")
    void getPostTest() throws Exception {
        Long request = presentId;
        Post want = postRepository.findById(request)
            .orElseThrow(() -> new NotExistPostException(NOT_EXIST_POST, request));
        PostResponse.SinglePost response = PostStub.noDependencySinglePostResponse(want);

        ResultActions resultActions = mockMvc.perform(
            get("/posts/{id}", request)
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.id").value(response.id()))
            .andExpect(jsonPath("data.seller.id").value(response.seller().id()))
            .andExpect(jsonPath("data.seller.nickName").value(response.seller().nickName()))
            .andExpect(jsonPath("data.seller.mannerTemperature").value(
                response.seller().mannerTemperature()))
            .andExpect(jsonPath("data.seller.role").value(response.seller().role()))
            .andExpect(jsonPath("data.seller.imagePath").value(response.seller().imagePath()))
            .andExpect(jsonPath("data.price").value(response.price()))
            .andExpect(jsonPath("data.title").value(response.title()))
            .andExpect(jsonPath("data.content").value(response.content()))
            .andExpect(jsonPath("data.postStatus").value(response.postStatus()))
            .andExpect(jsonPath("data.category").value(response.category()))
            .andExpect(jsonPath("data.attentionCount").value(response.attentionCount()))
            .andExpect(jsonPath("data.commentCount").value(response.commentCount()))
            .andExpect(jsonPath("data.likeOfMe").value(response.likeOfMe()))
            .andExpect(jsonPath("data.imagePaths").value(response.imagePaths()));

    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 최신순 정렬 조회 테스트")
    void getPostsLatestTest() throws Exception {
        PostResponse.Posts response = PostStub.posts(
            postRepository.findById(presentId - 2).get(),
            postRepository.findById(presentId - 1).get(),
            postRepository.findById(presentId).get()
        );

        ResultActions resultActions = mockMvc.perform(
            get("/posts")
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.posts[0].id").value(response.posts().get(0).id()))
            .andExpect(jsonPath("data.posts[1].id").value(response.posts().get(1).id()))
            .andExpect(jsonPath("data.posts[2].id").value(response.posts().get(2).id()));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 가격 기준 정렬 조회 테스트")
    void getPostsPriceTest() throws Exception {
        PostResponse.Posts response = PostStub.posts(
            postRepository.findById(presentId).get(),
            postRepository.findById(presentId - 2).get(),
            postRepository.findById(presentId - 1).get()
        );

        ResultActions resultActions = mockMvc.perform(
            get("/posts")
                .param("sort", "price")
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.posts[0].id").value(response.posts().get(0).id()))
            .andExpect(jsonPath("data.posts[1].id").value(response.posts().get(1).id()))
            .andExpect(jsonPath("data.posts[2].id").value(response.posts().get(2).id()));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("판매글 카테고리 조회")
    void getPostsByCategoryTest() throws Exception {
        PostResponse.Posts response = PostStub.posts(
            postRepository.findById(presentId - 2).get(),
            postRepository.findById(presentId).get()
        );

        ResultActions resultActions = mockMvc.perform(
            get("/posts")
                .param("category", "BEAUTY")
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.posts[0].id").value(response.posts().get(0).id()))
            .andExpect(jsonPath("data.posts[1].id").value(response.posts().get(1).id()));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("찜하기 기능 테스트")
    void attentionTest() throws Exception {
        Long request = presentId;
        Long response = 1L;
        ResultActions resultActions = mockMvc.perform(
            post("/posts/{id}/attention", request)
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.likeCount").value(response));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("찜한 판매글 조회 기능 테스트")
    void allAttentionTest() throws Exception {
        Long request = presentId;
        PostResponse.Posts response = PostStub.posts(
            postRepository.findById(request).get()
        );

        mockMvc.perform(
            post("/posts/{id}/attention", request)
        );

        ResultActions resultActions = mockMvc.perform(
            get("/posts/attention")
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.posts[0].id").value(response.posts().get(0).id()));
    }

    @Test
    @WithMockUser(username = "test", password = "test1234!@#$", roles = "USER")
    @DisplayName("제목 기준 검색 테스트")
    void searchTest() throws Exception {
        String request = "2";
        PostResponse.Posts response = PostStub.posts(
            postRepository.findById(presentId).get()
        );

        ResultActions resultActions = mockMvc.perform(
            get("/posts/search")
                .param("word", request)
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("data.posts[0].id").value(response.posts().get(0).id()));
    }

    @Disabled
    @WithMockUser(username = "buyer", password = "test1234!@#$", roles = "USER")
    private void registerCommand(Long id) throws Exception {
        CommentRequest.Save comment = new Save("comments");

        mockMvc.perform(
            post("/posts/{postId}/comments", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(comment))
        );
    }
}
