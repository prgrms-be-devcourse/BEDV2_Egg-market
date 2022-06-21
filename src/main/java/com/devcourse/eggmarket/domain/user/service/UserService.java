package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;

public interface UserService {

  public UserResponse save(UserRequest.Save userRequest);

  public UserResponse getById(Long id);

  public UserResponse update(Long id, UserRequest.Update userRequest);

  public boolean deleteById(Long id);
}
