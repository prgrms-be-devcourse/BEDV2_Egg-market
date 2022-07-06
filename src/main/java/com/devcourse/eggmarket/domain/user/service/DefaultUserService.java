package com.devcourse.eggmarket.domain.user.service;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.devcourse.eggmarket.domain.model.image.ImageUpload;
import com.devcourse.eggmarket.domain.model.image.ProfileImageFile;
import com.devcourse.eggmarket.domain.user.converter.UserConverter;
import com.devcourse.eggmarket.domain.user.dto.UserRequest;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Save;
import com.devcourse.eggmarket.domain.user.dto.UserRequest.Update;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.FindNickName;
import com.devcourse.eggmarket.domain.user.dto.UserResponse.Simple;
import com.devcourse.eggmarket.domain.user.exception.NotExistUserException;
import com.devcourse.eggmarket.domain.user.model.User;
import com.devcourse.eggmarket.domain.user.repository.UserRepository;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@Slf4j
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
    public Long save(Save userRequest) {
        User user = userRepository.save(userConverter.saveToUser(userRequest));

        Optional.ofNullable(userRequest.profileImage())
            .ifPresent(multipart ->
                supplyAsync(() -> uploadFile(user, multipart))
                    .thenAccept(user::setImagePath)
                    .exceptionally(ex -> {
                        log.error(ex.getMessage());
                        return null;
                    })
            );

        return user.getId();
    }

    @Override
    public UserResponse.Basic getByUsername(String userName) {
        Optional<User> user = userRepository.findByNickName(userName);
        if (user.isEmpty()) {
            throw new NotExistUserException();
        }

        return userConverter.convertToBasic(user.get());
    }

    @Override
    @Transactional
    public Long updateUserInfo(User user, Update userRequest) {
        updatePhoneNumber(user, userRequest.phoneNumber());
        updateNickName(user, userRequest.nickName());

        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public UserResponse.UpdateProfile updateUserProfile(User user, MultipartFile profile) {
        updateProfile(user, profile);
        User updateUser = userRepository.save(user);

        return userConverter.convertToUpdateProfile(updateUser);
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

        return userConverter.convertToNickName(foundUser.get());
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
    public Simple getUserBySimple(Long userId) {
        User user = getUserById(userId);

        return userConverter.convertToSimple(user);
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

    private void updatePhoneNumber(User user, String changePhoneNumber) {
        if (!user.getPhoneNumber().equals(changePhoneNumber)) {
            if (userRepository.findByPhoneNumber(changePhoneNumber).isPresent()) {
                throw new DuplicateKeyException("이미 등록된 번호입니다.");
            }
            user.changePhoneNumber(changePhoneNumber);
        }
    }

    private void updateNickName(User user, String changeNickName) {
        if (!user.getNickName().equals(changeNickName)) {
            if (userRepository.findByNickName(changeNickName).isPresent()) {
                throw new DuplicateKeyException("해당 닉네임은 이미 등록되어 있습니다.");
            }
            user.changeNickName(changeNickName);
        }

    }

    private void updateProfile(User user, MultipartFile profile) {
        String profilePath = user.getImagePath();

        if (profilePath != null) {
            imageUpload.deleteFile(profilePath);
        }
        imageUpload.upload(
            ProfileImageFile.toImage(user.getId(), profile)
        );
    }

    private String uploadFile(User user, MultipartFile multipartFile) {
        return imageUpload.upload(
            ProfileImageFile.toImage(user.getId(), multipartFile));
    }


}
