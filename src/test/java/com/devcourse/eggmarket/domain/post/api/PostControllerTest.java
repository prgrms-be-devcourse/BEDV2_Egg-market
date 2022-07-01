package com.devcourse.eggmarket.domain.post.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.service.PostAttentionService;
import com.devcourse.eggmarket.domain.post.service.PostService;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.domain.stub.UserStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PostControllerTest {

    @MockBean
    private PostService postService;

    @MockBean
    private PostAttentionService postAttentionService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser
    @DisplayName("판매글 작성 테스트")
    void writeTest() throws Exception {
        PostRequest.Save request = PostStub.writeRequest();
        Long response = 1L;

        String images = "Image Files";
        MockMultipartFile image = new MockMultipartFile("images", "img.png", "image/png",
            images.getBytes(StandardCharsets.UTF_8));

        doReturn(response)
            .when(postService)
            .save(any(PostRequest.Save.class), anyString());

        ResultActions resultActions = mockMvc.perform(
            multipart("/posts").file(image)
                .param("title", request.title())
                .param("content", request.content())
                .param("price", String.valueOf(request.price()))
                .param("category", request.category())
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isCreated())
            .andDo(document("post-write",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("title").description("제목"),
                    parameterWithName("content").description("본문"),
                    parameterWithName("price").description("가격"),
                    parameterWithName("category").description("카테고리")
                ),
                requestParts(
                    partWithName("images").description("이미지")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
            ));
    }

    @ParameterizedTest
    @MethodSource("com.devcourse.eggmarket.domain.stub.PostStub#invalidWriteRequest")
    @WithMockUser
    @DisplayName("판매글 생성시 잘못된 값을 전달할 경우 badRequest(400) 반환 테스트")
    void invalidRequestWriteTest(PostRequest.Save request) throws Exception {
        String images = "Image Files";
        MockMultipartFile image = new MockMultipartFile("images", "img.png", "image/png",
            images.getBytes(StandardCharsets.UTF_8));

        ResultActions resultActions = mockMvc.perform(
            multipart("/posts").file(image)
                .param("title", request.title())
                .param("content", request.content())
                .param("price", String.valueOf(request.price()))
                .param("category", request.category())
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("판매글 수정 테스트")
    void updatePostTest() throws Exception {
        PostRequest.UpdatePost request = PostStub.updatePostRequest();
        Long postId = 1L;

        doReturn(postId)
            .when(postService)
            .updatePost(anyLong(), any(PostRequest.UpdatePost.class), anyString());

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isOk())
            .andDo(document("post-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("본문"),
                    fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("판매글 ID")
                )
            ));

    }

    @ParameterizedTest
    @WithMockUser
    @MethodSource("com.devcourse.eggmarket.domain.stub.PostStub#invalidUpdatePostRequest")
    @DisplayName("판매글 수정시 잘못된 값을 전달할 경우 badRequest(400) 반환 테스트")
    void invalidUpdatePostTest(PostRequest.UpdatePost request) throws Exception {
        Long postId = 1L;

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("판매글 상태변경 테스트")
    void updatePurchaseTest() throws Exception {
        PostRequest.UpdatePurchaseInfo request = PostStub.updatePurchaseInfo();
        Long postId = 1L;

        doReturn(postId)
            .when(postService)
            .updatePurchaseInfo(anyLong(), any(PostRequest.UpdatePurchaseInfo.class), anyString());

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/posts/" + postId + "/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isOk())
            .andDo(document("post-update-purchase",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("postStatus").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("buyerId").type(JsonFieldType.NUMBER).description("구매자 ID")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("판매글 ID")
                )
            ));
    }

    @ParameterizedTest
    @WithMockUser
    @MethodSource("com.devcourse.eggmarket.domain.stub.PostStub#invalidUpdatePurchaseInfoRequest")
    @DisplayName("판매글 상태변경시 잘못된 값을 전달할 경우 badRequest(400) 반환 테스트")
    void invalidUpdatePurchaseTest(PostRequest.UpdatePurchaseInfo request) throws Exception {
        Long postId = 1L;

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/posts/" + postId + "/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("판매글 삭제 테스트")
    void deleteTest() throws Exception {
        Long request = 1L;

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/posts/" + request)
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isNoContent())
            .andDo(document("post-delete"));
    }

    @Test
    @WithMockUser
    @DisplayName("판매글 단일 조회 테스트")
    void getPostTest() throws Exception {
        Long request = 1L;
        PostResponse.SinglePost response = PostStub.singlePostResponse(
            PostStub.entity(
                UserStub.entity()
            )
        );
        doReturn(response)
            .when(postService)
            .getById(anyLong(), anyString());

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/posts/" + request)
        );

        resultActions.andExpect(status().isOk())
            .andDo(document("post-get-detail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("판매글 ID"),
                    fieldWithPath("data.seller.id").type(JsonFieldType.NUMBER)
                        .description("판매자 ID"),
                    fieldWithPath("data.seller.nickName").type(JsonFieldType.STRING)
                        .description("판매자 닉네임"),
                    fieldWithPath("data.seller.mannerTemperature").type(JsonFieldType.NUMBER)
                        .description("판매자 매너 온도"),
                    fieldWithPath("data.seller.role").type(JsonFieldType.STRING)
                        .description("판매자 권한"),
                    fieldWithPath("data.seller.imagePath").type(JsonFieldType.VARIES)
                        .description("판매자 프로필 이미지"),
                    fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("본문"),
                    fieldWithPath("data.postStatus").type(JsonFieldType.STRING)
                        .description("판매 상태"),
                    fieldWithPath("data.category").type(JsonFieldType.STRING).description("카테고리"),
                    fieldWithPath("data.createAt").type(JsonFieldType.STRING)
                        .description("판매글 생성 시간"),
                    fieldWithPath("data.attentionCount").type(JsonFieldType.NUMBER)
                        .description("찜 개수"),
                    fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER)
                        .description("댓글 개수"),
                    fieldWithPath("data.likeOfMe").type(JsonFieldType.BOOLEAN).description("찜 여부"),
                    fieldWithPath("data.imagePaths").type(JsonFieldType.VARIES)
                        .description("판매글 이미지 링크")
                )
            ));
    }

    @Test
    @DisplayName("판매글 최신순 조회 테스트")
    void getPostsLatestTest() {
        PostResponse.Posts response = PostStub.posts();

        doReturn(response)
            .when(postService)
            .getAll(any(Pageable.class));

//        ResultActions resultActions = mockMvc.perform();
    }

    @Test
    @DisplayName("판매글 가격순 조회 테스트")
    void getPostsPriceTest() {
        PostResponse.Posts response = PostStub.priceSortPosts();
    }
}