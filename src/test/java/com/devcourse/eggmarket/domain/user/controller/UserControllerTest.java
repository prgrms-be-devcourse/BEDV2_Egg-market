package com.devcourse.eggmarket.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devcourse.eggmarket.domain.model.image.ImageFile;
import com.devcourse.eggmarket.domain.stub.ImageStub;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.FindNickname;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import com.devcourse.eggmarket.domain.user.service.UserService;
import com.devcourse.eggmarket.global.common.SuccessResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    User user;
    Long userIdResponse;
    MockMultipartFile profileImage;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .nickName("test1")
            .phoneNumber("010-1234-5678")
            .password("Password!1")
            .role("USER")
            .build();

        String image = "profile";

        profileImage = new MockMultipartFile(
            "profileImage",
            "img.png",
            "image/png",
            image.getBytes(StandardCharsets.UTF_8));

        UserRequest.Save saveRequest = new Save(user.getPhoneNumber(), user.getNickName(),
            user.getPassword(), false, null);

        userIdResponse = userService.save(saveRequest);
    }

    @AfterEach
    void clearUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 기능 테스트")
    void signUp() throws Exception {
        //Given
        User newUser = User.builder()
            .nickName("test2")
            .phoneNumber("010-1111-1111")
            .password("Password!2")
            .role("USER")
            .build();

        SuccessResponse<Long> expectResponse =
            new SuccessResponse<>(2L);

        UserRequest.Save saveRequest = new UserRequest.Save(
            newUser.getPhoneNumber(),
            newUser.getNickName(),
            newUser.getPassword(),
            false,
            profileImage
        );
        //when
        MvcResult result = mockMvc.perform(
                multipart("/signup")
                    .file(profileImage)
                    .param("nickName", saveRequest.nickName())
                    .param("phoneNumber", saveRequest.phoneNumber())
                    .param("password", saveRequest.password())
                    .param("isAdmin", saveRequest.isAdmin() ? "true" : "false")
            )
            .andExpect(status().isCreated())
            .andDo(document("user-signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("nickName").description("유저 닉네임"),
                    parameterWithName("phoneNumber").description("휴대폰 번호"),
                    parameterWithName("password").description("패스워드"),
                    parameterWithName("isAdmin").description("관리자 여부")
                ),
                requestParts(
                    partWithName("profileImage").description("프로필 사진")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("사용자 ID")
                )
            ))
            .andReturn();

        SuccessResponse<Long> response = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });

        //Then
        assertThat(response.getData())
            .isEqualTo(expectResponse.getData());
    }

    @Test
    @DisplayName("로그인 기능 테스트")
    void login() throws Exception {
        //Given
        UserResponse.Basic expectResponse = UserResponse.Basic.builder()
            .nickName(user.getNickName())
            .mannerTemperature(36.5F)
            .role(user.getRole().toString())
            .build();

        UserRequest.Login loginRequest = new UserRequest.Login(user.getNickName(),
            user.getPassword());
        SuccessResponse<Boolean> response = new SuccessResponse<>(true);
        //When
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andDo(document("user-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("로그인 성공 여부")
                )
            ))
            .andReturn();

        SuccessResponse<Boolean> loginResult = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });

        //Then
        assertThat(loginResult).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("로그아웃 기능 테스트")
    void logoutTest() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);

        MvcResult mvcResult = mockMvc.perform(
                post("/logout")
                    .session(session))
            .andExpect(status().isOk())
            .andDo(document("user-logout",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Logout Success");
    }

    @Test
    @DisplayName("회원 탈퇴 기능 테스트")
    void signOut() throws Exception {
        //Given
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);
        SuccessResponse<Long> response = new SuccessResponse<>(userIdResponse);

        //When
        MvcResult result = mockMvc.perform(
                delete("/signout")
                    .session(session))
            .andExpect(status().isOk())
            .andDo(document("user-signout",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 ID")
                ))
            )
            .andReturn();

        SuccessResponse<Long> signOutResult = objectMapper.readValue(
            result.getResponse().getContentAsString(), new TypeReference<>() {
            }
        );

        //Then
        assertThat(signOutResult.getData()).isEqualTo(response.getData());
    }

    @Test
    @DisplayName("닉네임으로 회원정보 불러오기")
    void getUserName() throws Exception {
        //Given
        SuccessResponse<UserResponse.FindNickName> expectResult = new SuccessResponse<>(
            UserResponse.FindNickName.builder()
                .nickName(user.getNickName()).build()
        );
        UserRequest.FindNickname request = new FindNickname(user.getPhoneNumber());
        //When
        MvcResult result = mockMvc.perform(
                get("/users/nickname")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .param("phoneNumber", request.phoneNumber()))
            .andExpect(status().isOk())
            .andDo(document("user-findName",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("phoneNumber").description("휴대폰 번호")
                ),
                responseFields(
                    fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("닉네임")
                )
            ))
            .andReturn();

        SuccessResponse<UserResponse.FindNickName> findResult = objectMapper
            .readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        //Then
        assertThat(findResult).usingRecursiveComparison().isEqualTo(expectResult);
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePassword() throws Exception {
        //Given
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);
        SuccessResponse<Boolean> response = new SuccessResponse<>(true);
        UserRequest.ChangePassword userRequest = new UserRequest.ChangePassword(
            "NewPass!1"
        );

        //When
        MvcResult result = mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isOk())
            .andDo(document("user-change-password",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("newPassword").type(JsonFieldType.STRING).description("변경할 패스워드")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("성공 여부")
                )
            ))
            .andReturn();

        SuccessResponse<Boolean> updateResult = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {
            }
        );

        //Then
        assertThat(updateResult).usingRecursiveComparison().isEqualTo(response);
    }

    @Test
    @DisplayName("유저 정보 변경 테스트")
    void updateUserInfoTest() throws Exception {
        //Given
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);

        UserRequest.Update userRequest = new Update("010-1111-1111", "updateNick");
        SuccessResponse<Long> expectResponse = new SuccessResponse<>(userIdResponse);

        //When
        MvcResult result = mockMvc.perform(
                put("/users/profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest))
            )
            .andExpect(status().isOk())
            .andDo(document("user-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 ID")
                )
            ))
            .andReturn();

        SuccessResponse<Long> updateResult = objectMapper
            .readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        //Then
        assertThat(updateResult).usingRecursiveComparison()
            .isEqualTo(expectResponse);

    }

    @Test
    @DisplayName("유저 프로필 이미지 변경 테스트")
    void updateProfileTest() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);

        MockMultipartFile image = ImageStub.image1();
        MvcResult result = mockMvc.perform(
                multipart("/users/profile/image")
                    .file(image)
            )
            .andExpect(status().isCreated())
            .andDo(document("user-update-profile",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParts(
                    partWithName("image").description("유저 프로필 이미지")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("유저 ID")
                )
            ))
            .andReturn();

    }

    @Test
    @DisplayName("")
    void getMannerTemperature() throws Exception {
        //Given
        SuccessResponse<UserResponse.MannerTemperature> expectResponse = new SuccessResponse<>(
            UserResponse.MannerTemperature.builder()
                .id(userIdResponse)
                .nickName(user.getNickName())
                .mannerTemperature(user.getMannerTemperature())
                .build()
        );

        //When
        MvcResult result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/users/mannerTemperature/{id}",
                    userIdResponse))
            .andExpect(status().isOk())
            .andDo(document("user-get-temperature",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("사용자 ID")
                ),
                responseFields(
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 ID"),
                    fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("data.mannerTemperature").type(JsonFieldType.NUMBER)
                        .description("매너 온도")
                )
            ))
            .andReturn();

        SuccessResponse<UserResponse.MannerTemperature> mannerTempResult = objectMapper
            .readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        //Then
        assertThat(mannerTempResult).usingRecursiveComparison().isEqualTo(expectResponse);
    }

}