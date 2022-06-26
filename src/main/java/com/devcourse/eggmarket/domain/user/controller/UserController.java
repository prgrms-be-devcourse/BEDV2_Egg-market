package com.devcourse.eggmarket.domain.user.controller;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public final UserService userService;
    public final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public UserResponse.Basic signUp(UserRequest.Save request) {
        return userService.save(request);
    }

    @PostMapping("/login")
    public boolean login(@RequestBody UserRequest.Login userRequest, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(userRequest.nickName());

        if (!passwordEncoder.matches(userRequest.password(), userDetails.getPassword())) {
            throw new IllegalArgumentException();
        }

        createNewSession(request, userDetails, securityContext);
        return true;
    }

    @DeleteMapping("/signout")
    public Long signOut(HttpServletRequest request, Authentication authentication) {
        User user = userService.getUser(authentication);
        Long userId = userService.delete(user);

        deleteSession(request);

        return userId;
    }

    private void deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/users/nickName")
    public UserResponse.FindNickName findNickName(@RequestParam String phoneNumber) {
        return userService.getUserName(phoneNumber);
    }

    @PatchMapping("/users/password")
    public boolean changePassword(HttpServletRequest request, Authentication authentication,
        @RequestBody UserRequest.ChangePassword userRequest) {
        User user = userService.getUser(authentication);
        boolean result = userService.updatePassword(user, userRequest);

        deleteSession(request);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());

        createNewSession(request, userDetails, securityContext);

        return result;
    }

    private void createNewSession(HttpServletRequest request, UserDetails userDetails,
        SecurityContext securityContext) {
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);
    }

    @PutMapping("/users")
    public UserResponse.Update update(HttpServletRequest request, Authentication authentication,
        @RequestBody UserRequest.Update userRequest) {
        User user = userService.getUser(authentication);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = userService.loadUserByUsername(user.getNickName());

        createNewSession(request, userDetails, securityContext);

        return userService.update(user, userRequest);
    }

    @GetMapping("/users/mannerTemperature")
    public UserResponse.MannerTemperature getMannerTemperature(@RequestParam Long id) {
        return userService.getMannerTemperature(id);
    }

}