package com.devcourse.eggmarket.users.service;

import com.devcourse.eggmarket.users.dto.UserRequest;
import com.devcourse.eggmarket.users.dto.UserResponse;
import org.springframework.stereotype.Service;

public interface UserService {

  public UserResponse save(UserRequest userRequest);

}
