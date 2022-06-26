package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.ChangePassword;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.FindNickName;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.MannerTemperature;
import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse.Basic save(UserRequest.Save userRequest);

    UserResponse.Basic getByUsername(String userName);

    UserResponse.Update update(User user, UserRequest.Update userRequest);

    User getUser(Authentication authentication);

    Long delete(User user);

    UserResponse.FindNickName getUserName(String phoneNumber);

    boolean updatePassword(User user, UserRequest.ChangePassword userRequest);

    User getUserById(Long userId);

    MannerTemperature getMannerTemperature(Long userId);
}
