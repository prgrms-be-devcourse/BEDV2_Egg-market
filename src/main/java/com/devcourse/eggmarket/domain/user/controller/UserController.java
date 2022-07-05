package com.devcourse.eggmarket.domain.user.controller;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import com.devcourse.eggmarket.global.common.SuccessResponse;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

    public final UserService userService;
    public final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<UserResponse.Basic>> signUp(
        @ModelAttribute @Valid UserRequest.Save request) {
        UserResponse.Basic response = userService.save(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

        return ResponseEntity.created(location)
            .body(new SuccessResponse<>(response));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Boolean>> login(
        @RequestBody @Valid UserRequest.Login userRequest, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(userRequest.nickName());

        if (!passwordEncoder.matches(userRequest.password(), userDetails.getPassword())) {
            throw new IllegalArgumentException();
        }

        createNewSession(request, userDetails, securityContext);
        return ResponseEntity.ok(new SuccessResponse<>(true));
    }


    @DeleteMapping("/signout")
    public ResponseEntity<SuccessResponse<Long>> signOut(HttpServletRequest request,
        Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        Long userId = userService.delete(user);

        deleteSession(request);

        return ResponseEntity.ok(new SuccessResponse<>(userId));
    }

    private void deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping(value = "/users/nickname", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<SuccessResponse<UserResponse.FindNickName>> findNickname(
        @ModelAttribute @Valid UserRequest.FindNickname userRequest) {
        return ResponseEntity.ok(
            new SuccessResponse<>(
                userService.getUserName(userRequest.phoneNumber())
            )
        );
    }


    @PatchMapping("/users/password")
    public ResponseEntity<SuccessResponse<Boolean>> changePassword(HttpServletRequest request,
        Authentication authentication,
        @RequestBody UserRequest.ChangePassword userRequest) {
        User user = userService.getUser(authentication.getName());
        boolean result = userService.updatePassword(user, userRequest);

        deleteSession(request);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());

        createNewSession(request, userDetails, securityContext);

        return ResponseEntity.ok(new SuccessResponse<>(result));
    }

    private void createNewSession(HttpServletRequest request, UserDetails userDetails,
        SecurityContext securityContext) {
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);
    }

    @PutMapping("/users")
    public ResponseEntity<SuccessResponse<UserResponse.Update>> update(HttpServletRequest
        request,
        Authentication authentication,
        @RequestBody @Valid UserRequest.Update userRequest) {
        User user = userService.getUser(authentication.getName());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());

        createNewSession(request, userDetails, securityContext);

        return ResponseEntity.ok(
            new SuccessResponse<>(userService.update(user, userRequest)));
    }

    @GetMapping("/users/mannerTemperature/{id}")
    public ResponseEntity<SuccessResponse<UserResponse.MannerTemperature>> getMannerTemperature
        (
            @PathVariable Long id) {
        return ResponseEntity.ok(
            new SuccessResponse<>(userService.getMannerTemperature(id)));
    }

}