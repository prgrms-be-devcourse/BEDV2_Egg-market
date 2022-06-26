package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.PostAttention;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostAttentionRepository extends JpaRepository<PostAttention, Long> {
    @Query("select pa from PostAttention pa where pa.post.id = :postId and pa.user.id = :userId")
    Optional<PostAttention> findByPostIdAndUserId(Long postId, Long userId);

    @Query("select count(pa) from PostAttention pa where pa.post.id = :postId")
    int countPostLikeByPost(Long postId);
}
