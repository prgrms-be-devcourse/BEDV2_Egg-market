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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Save;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Update;
import com.devcourse.eggmarket.domain.post.service.PostAttentionService;
import com.devcourse.eggmarket.domain.post.service.PostService;
import com.devcourse.eggmarket.domain.stub.PostStub;
import com.devcourse.eggmarket.global.error.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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
        PostResponse.Save response = new Save(1L);

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
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("존재하지 않는 카테고리가 전달될 경우 예외가 발생하는지 테스트")
    void invalidRequestWriteTest() throws Exception {
        PostRequest.Save request = PostStub.invalidCategoryWriteRequest();
        String images = "Image Files";
        MockMultipartFile image = new MockMultipartFile("images", "img.png", "image/png",
            images.getBytes(StandardCharsets.UTF_8));

        ResultActions resultActions = mockMvc.perform(
            multipart("/posts").file(image)
                .param("title"," ")
                .param("content", request.content())
                .param("price", "aa")
                .param("category", request.category())
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.NOT_VALID_CATEGORY.getMessage()));
    }

}