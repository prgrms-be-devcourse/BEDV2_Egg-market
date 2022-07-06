package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Simple;
import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    Long save(UserRequest.Save userRequest);

    UserResponse.Basic getByUsername(String userName);

    User getById(Long userId);

    User getUser(String nickName);

    Long delete(User user);

    UserResponse.FindNickName getUserName(String phoneNumber);

    boolean updatePassword(User user, UserRequest.ChangePassword userRequest);

    User getUserById(Long userId);

    Simple getUserBySimple(Long userId);

    Long updateUserInfo(User user, Update userRequest);

    UserResponse.UpdateProfile updateUserProfile(User user, MultipartFile profile);
}
