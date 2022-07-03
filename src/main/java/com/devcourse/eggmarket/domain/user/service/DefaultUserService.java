package com.devcourse.eggmarket.domain.user.service;

import com.devcourse.eggmarket.domain.model.image.ImageFile;
import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.ProfileImageFile;
import com.devcourse.eggmarket.domain.user.converter.UserConverter;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.FindNickName;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.MannerTemperature;
import com.devcourse.eggmarket.domain.user.exception.NotExistUserException;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
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
            ImageFile imageFile = ProfileImageFile.toImage(user.getId(),
                userRequest.profileImage());
            user.setImagePath(imageUpload.upload(imageFile));
        }
        return userConverter.convertToUserResponseBasic(user);
    }

    @Override
    public UserResponse.Basic getByUsername(String userName) {
        Optional<User> user = userRepository.findByNickName(userName);
        if (user.isEmpty()) {
            throw new NotExistUserException();
        }

        return userConverter.convertToUserResponseBasic(user.get());
    }

    @Override
    @Transactional
    public UserResponse.Update update(User user, Update userRequest) {
        if (!user.getPhoneNumber().equals(userRequest.phoneNumber())
            && userRequest.phoneNumber() != null) {
            Optional<User> phoneNumberResult = userRepository
                .findByPhoneNumber(userRequest.phoneNumber());
            if (phoneNumberResult.isPresent()) {
                throw new DuplicateKeyException("해당 전화번호는 이미 등록되어 있습니다.");
            }
            user.changePhoneNumber(userRequest.phoneNumber());
        }

        if (!user.getNickName().equals(userRequest.nickName()) && userRequest.nickName() != null) {
            Optional<User> nickNameResult = userRepository.findByNickName(userRequest.nickName());
            if (nickNameResult.isPresent()) {
                throw new DuplicateKeyException("해당 닉네임은 이미 등록되어 있습니다.");
            }
            user.changeNickName(userRequest.nickName());
        }

        if (userRequest.profileImage() != null) {
            ImageFile image = ProfileImageFile.toImage(user.getId(), userRequest.profileImage());
            user.setImagePath(imageUpload.upload(image));
        }

        userRepository.save(user);

        return userConverter.convertToUpdate(user);
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
    public User getUser(String nickName) {
        Optional<User> user = userRepository.findByNickName(nickName);
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

    public User getById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(NotExistUserException::new);
    }

    @Override
    public FindNickName getUserName(String phoneNumber) {
        Optional<User> foundUser = userRepository.findByPhoneNumber(phoneNumber);
        if (foundUser.isEmpty()) {
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
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("해당 아이디를 가진 유저는 존재하지 않습니다.");
        }

        return user.get();
    }

    @Override
    public MannerTemperature getMannerTemperature(Long userId) {
        User user = getUserById(userId);

        return userConverter.convertToMannerTemp(user);
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
