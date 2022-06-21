package com.devcourse.eggmarket.domain.user.repository;

import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
