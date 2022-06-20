package com.devcourse.eggmarket.domain.users.service;

import com.devcourse.eggmarket.domain.users.dto.UserRequest;
import com.devcourse.eggmarket.domain.users.dto.UserResponse;

public interface UserService {

  public UserResponse save(UserRequest userRequest);

}
