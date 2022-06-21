package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Long, Post> {

}
