package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.model.image.Image;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.ProfileImage;
import com.devcourse.eggmarket.domain.user.converter.UserConverter;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Collections;
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
        Image image = ProfileImage.toImage(user.getId(), userRequest.profileImage());
        user.setImagePath(imageUpload.upload(image));
        return userConverter.convertToUserResponse(user);
    }

    @Override
    public UserResponse getById(Long id) {
        return null;
    }

    @Override
    public UserResponse update(Long id, Update userRequest) {
        return null;
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
