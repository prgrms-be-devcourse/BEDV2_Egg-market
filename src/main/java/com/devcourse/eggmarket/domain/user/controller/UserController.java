package com.devcourse.eggmarket.domain.user.controller;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public UserResponse signUp(UserRequest.Save request) {
        return userService.save(request);
    }

    @PostMapping("/login")
    public boolean login(@RequestBody UserRequest.Login userRequest, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = userService.loadUserByUsername(userRequest.nickName());
        if (!passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException();
        }
        securityContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            securityContext);
        return true;
    }

}