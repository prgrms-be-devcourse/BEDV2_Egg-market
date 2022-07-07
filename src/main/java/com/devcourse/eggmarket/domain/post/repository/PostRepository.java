package com.devcourse.eggmarket.domain.post.repository;

import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p "
        + "from Post p inner join PostAttention pa on pa.post.id = p.id "
        + "inner join fetch p.seller "
        + "WHERE pa.user.id = :userId "
        + "AND p.id > :lastId "
        + "ORDER BY p.id DESC")
    List<Post> findAllLikedBy(@Param("userId") Long userId, Long lastId, Pageable pageable);

    List<Post> findAllByCategory(Pageable pageable, Category category);

    List<Post> findAllByTitleContains(Pageable pageable, String word);
}
