package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;

public interface UserService {

    UserResponse save(UserRequest.Save userRequest);

    UserResponse getById(Long id);

    UserResponse update(Long id, UserRequest.Update userRequest);

    boolean deleteById(Long id);
}
