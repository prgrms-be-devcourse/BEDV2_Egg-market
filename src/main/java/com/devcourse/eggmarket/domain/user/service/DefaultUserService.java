package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.model.image.Image;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.ProfileImage;
import com.devcourse.eggmarket.domain.user.converter.UserConverter;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.exception.NotExistUserException;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserResponse save(Save userRequest) {
        User user = userRepository.save(userConverter.saveToUser(userRequest));
        if (userRequest.profileImage() != null) {
            Image image = ProfileImage.toImage(user.getId(), userRequest.profileImage());
            user.setImagePath(imageUpload.upload(image));
        }
        return userConverter.convertToUserResponse(user);
    }

    @Override
    public UserResponse getByUsername(String userName) {
        Optional<User> user = userRepository.findByNickName(userName);
        if (user.isEmpty()) {
            throw new NotExistUserException();
        }

        return userConverter.convertToUserResponse(user.get());
    }

    @Override
    public UserResponse update(String userName, Update userRequest) {
        return null;
    }

    @Override
    public User getUser(String userName) {
        Optional<User> user = userRepository.findByNickName(userName);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 닉네임을 가진 사용자가 존재하지 않습니다");
        }

        return user.get();
    }

    public User getById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(NotExistUserException::new);
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
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
