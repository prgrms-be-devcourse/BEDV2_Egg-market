package com.devcourse.eggmarket.domain.evaluation.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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

import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest.Save;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationResponse;
import com.devcourse.eggmarket.domain.evaluation.service.EvaluationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(EvaluationController.class)
class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EvaluationService evaluationService;

    @Test
    @WithMockUser
    @DisplayName("?????? ?????? ?????????")
    void save() throws Exception {
        given(evaluationService.save(any(EvaluationRequest.Save.class)))
            .willReturn(4L);

        EvaluationRequest.Save save = new Save(
            1L,
            2L,
            3L,
            5,
            "?????? ???????????????."
        );

        ResultActions resultActions = this.mockMvc.perform(
            post("/evaluation")
                .content(objectMapper.writeValueAsString(save))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        resultActions.andExpect(status().isCreated())
            .andDo(document("evaluation-save",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("reviewerId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                    fieldWithPath("revieweeId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                    fieldWithPath("score").type(JsonFieldType.NUMBER).description("??????"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????")
                    ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????? ?????????")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ???????????? ?????? ?????????")
    void getByReviewerId() throws Exception {
        EvaluationResponse response = new EvaluationResponse(
            "reviewer",
            "?????? ?????? ???????????????."
        );

        given(evaluationService.getByReviewerId(any(Long.class)))
            .willReturn(response);

        ResultActions resultActions = this.mockMvc.perform(
            get("/evaluation/reviewer/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
            .andDo(document("evaluation-reviewer-get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("????????? ?????????")
                ),
                responseFields(
                    fieldWithPath("data.revieweeName").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("?????? ??????")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("????????? ???????????? ?????? ?????????")
    void getByRevieweeId() throws Exception {
        EvaluationResponse response = new EvaluationResponse(
            "reviewee",
            "?????? ?????? ???????????????."
        );

        given(evaluationService.getByRevieweeId(any(Long.class)))
            .willReturn(response);

        ResultActions resultActions = this.mockMvc.perform(
            get("/evaluation/reviewee/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
            .andDo(document("evaluation-reviewee-get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("????????? ?????????")
                ),
                responseFields(
                    fieldWithPath("data.revieweeName").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("?????? ??????")
                )
            ));
    }

    @Test
    @WithMockUser
    @DisplayName("?????? ?????????")
    void testDelete() throws Exception {
        willDoNothing().given(evaluationService).delete(any(Long.class));

        ResultActions resultActions = this.mockMvc.perform(
            delete("/evaluation/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );

        resultActions.andExpect(status().isNoContent())
            .andDo(document("evaluation-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("?????? ?????????")
                )
            ));
    }
}