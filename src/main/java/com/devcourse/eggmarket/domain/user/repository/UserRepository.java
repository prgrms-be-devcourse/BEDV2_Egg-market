package com.devcourse.eggmarket.domain.user.repository;

import com.devcourse.eggmarket.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByNickName(String nickName);

    public Optional<User> findByPhoneNumber(String phoneNumber);

}
