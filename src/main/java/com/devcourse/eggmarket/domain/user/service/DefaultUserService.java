package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.model.image.Image;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.ProfileImage;
import com.devcourse.eggmarket.domain.user.converter.UserConverter;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.ChangePassword;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.FindNickName;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final ImageUpload imageUpload;

    public DefaultUserService(
        UserRepository userRepository,
        UserConverter userConverter,
        ImageUpload imageUpload) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.imageUpload = imageUpload;
    }

    @Override
    @Transactional
    public UserResponse.Basic save(Save userRequest) {
        User user = userRepository.save(userConverter.saveToUser(userRequest));
        if (userRequest.profileImage() != null) {
            Image image = ProfileImage.toImage(user.getId(), userRequest.profileImage());
            user.setImagePath(imageUpload.upload(image));
        }
        return userConverter.convertToUserResponseBasic(user);
    }

    @Override
    public UserResponse.Basic getByUsername(String userName) {
        Optional<User> user = userRepository.findByNickName(userName);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 닉네임을 가진 사용자가 존재하지 않습니다");
        }

        return userConverter.convertToUserResponseBasic(user.get());
    }

    @Override
    public UserResponse update(String userName, Update userRequest) {
        return null;
    }

    @Override
    public User getUser(Authentication authentication) {
        if (authentication == null) {
            throw new SessionAuthenticationException("Login을 먼저 진행해주세요");
        }

        Optional<User> user = userRepository.findByNickName(authentication.getName());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 닉네임을 가진 사용자가 존재하지 않습니다");
        }

        return user.get();
    }

    @Override
    @Transactional
    public Long delete(User user) {
        userRepository.delete(user);
        return user.getId();
    }

    @Override
    public FindNickName getUserName(String phoneNumber) {
        Optional<User> foundUser = userRepository.findByPhoneNumber(phoneNumber);
        if (foundUser.isEmpty()){
            throw new NoSuchElementException("해당 핸드폰 번호를 가진 유저는 존재하지 않습니다.");
        }

        return userConverter.convertToUserFindNickName(foundUser.get());
    }

    @Override
    @Transactional
    public boolean updatePassword(User user, UserRequest.ChangePassword userRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.changePassword(passwordEncoder.encode(userRequest.newPassword()));
        userRepository.save(user);
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByNickName(username)
            .map(user ->
                org.springframework.security.core.userdetails.User.builder()
                    .username(user.getNickName())
                    .password(user.getPassword())
                    .authorities(Collections
                        .singletonList(new SimpleGrantedAuthority(user.getRole().toString())))
                    .build())
            .orElseThrow(
                () -> new UsernameNotFoundException("Could not found user for " + username));
    }
}
