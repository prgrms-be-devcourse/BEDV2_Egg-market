package com.devcourse.eggmarket.domain.comment.api;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Save;
import com.devcourse.eggmarket.domain.comment.dto.CommentRequest.Update;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.Comments;
import com.devcourse.eggmarket.domain.comment.dto.CommentResponse.CommentsElement;
import com.devcourse.eggmarket.domain.comment.service.CommentService;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Basic;
import com.devcourse.eggmarket.domain.user.model.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validator validator;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser
    @DisplayName("????????? ????????? ????????? ???????????? ?????? ????????? ????????????")
    void save() throws Exception {
        CommentRequest.Save request = new Save("first comment");
        Long postId = 1L;
        Long expectedResponse = 1L;

        doReturn(expectedResponse)
            .when(commentService)
            .write(anyString(), anyLong(), ArgumentMatchers.any(CommentRequest.Save.class));

        ResultActions resultActions = mockMvc.perform(
            post("/posts/{postId}/comments", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isCreated())
            .andDo(document("comment-write",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????")
                ),
                pathParameters(
                    parameterWithName("postId").description("????????? ID")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????? ID")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ????????? ????????? ????????? ????????????")
    void update() throws Exception {
        CommentRequest.Update updateRequest = new Update("modify comment!");
        Long postId = 1L;
        Long commentId = 1L;
        Long expectedResponse = 1L;

        ResultActions resultActions = mockMvc.perform(
            put("/posts/{postId}/comments/{commentId}", postId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest))
                .with(csrf().asHeader())
        );

        doReturn(expectedResponse)
            .when(commentService)
            .update(anyString(), anyLong(), anyLong(),
                ArgumentMatchers.any(CommentRequest.Update.class));

        resultActions.andExpect(status().isOk())
            .andDo(document("comment-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ?????? ????????? id"),
                    parameterWithName("commentId").description("?????? id")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????? ID")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ????????? ????????? ????????? ????????????")
    void delete() throws Exception {
        Long postId = 1L;
        Long commentId = 1L;

        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/posts/{postId}/comments/{commentId}", postId,
                    commentId)
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isNoContent())
            .andDo(document("comment-delete",
                preprocessRequest(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ?????? ????????? id"),
                    parameterWithName("commentId").description("?????? id")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("?????? ???????????? ?????? ???????????? ????????????")
    void all() throws Exception {
        Long postId = 1L;
        Long lastId = 1L;

        CommentResponse.Comments comments = new Comments(
            List.of(
                new CommentsElement(2L, new Basic(1L, "writer", 35.5f, UserRole.USER.name(), null),
                    "second comment", LocalDateTime.now())
            )
        );

        doReturn(comments)
            .when(commentService)
            .getAllComments(anyString(), anyLong(), anyLong());

        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/posts/{postId}/comments", postId)
                .param("lastId", lastId.toString())
                .with(csrf().asHeader())
        );

        resultActions.andExpect(status().isOk())
            .andDo(document("post-comments",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("????????? ?????? ????????? id")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????????"),
                    fieldWithPath("data.comments[]").type(JsonFieldType.ARRAY)
                        .description("????????? ??????"),
                    fieldWithPath("data.comments[].id").type(JsonFieldType.NUMBER)
                        .description("?????? ID"),
                    fieldWithPath("data.comments[].writer.id").type(JsonFieldType.NUMBER)
                        .description("?????? ????????? ID"),
                    fieldWithPath("data.comments[].writer.nickName").type(JsonFieldType.STRING)
                        .description("?????? ????????? ?????????"),
                    fieldWithPath("data.comments[].writer.mannerTemperature").type(
                            JsonFieldType.NUMBER)
                        .description("?????? ????????? ?????? ??????"),
                    fieldWithPath("data.comments[].writer.role").type(JsonFieldType.STRING)
                        .description("?????? ????????? ??????"),
                    fieldWithPath("data.comments[].writer.imagePath").type(JsonFieldType.VARIES)
                        .description("?????? ????????? ????????? ?????????"),
                    fieldWithPath("data.comments[].content").type(JsonFieldType.STRING)
                        .description("?????? ??????"),
                    fieldWithPath("data.comments[].createdAt").type(JsonFieldType.STRING)
                        .description("?????? ?????? ??????")
                )
            ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("???????????? ?????? ?????? ?????? ?????? ????????? ???????????? ????????? ????????????")
    void emptyCommentViolation(String content) {
        CommentRequest.Save saveRequest = new CommentRequest.Save(content);

        Set<ConstraintViolation<Save>> violations = validator.validate(saveRequest);

        Assertions.assertThat(violations.isEmpty())
            .isFalse();
    }
}