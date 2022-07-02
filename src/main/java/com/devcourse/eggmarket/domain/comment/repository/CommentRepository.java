package com.devcourse.eggmarket.domain.comment.repository;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPost(Post post, Pageable pageable);

    List<Comment> findAllByPostAndIdGreaterThan(Post post,
        @RequestParam("id") Long lastId,
        Pageable pageable);

    @Query("select c from Comment c join fetch c.post WHERE c.id = :commentId AND c.post.id = :postId")
    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);
}
