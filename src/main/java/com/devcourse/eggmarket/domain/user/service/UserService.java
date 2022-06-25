package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse.Basic save(UserRequest.Save userRequest);

    UserResponse.Basic getByUsername(String userName);

    UserResponse update(String userName, UserRequest.Update userRequest);

    User getUser(Authentication authentication);

    Long delete(User user);
}
