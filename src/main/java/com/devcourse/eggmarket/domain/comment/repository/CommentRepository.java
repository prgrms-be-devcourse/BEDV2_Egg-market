package com.devcourse.eggmarket.domain.comment.repository;

import com.devcourse.eggmarket.domain.comment.model.Comment;
import com.devcourse.eggmarket.domain.post.model.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    @Query("select c from Comment c join fetch c.post WHERE c.id = :commentId AND c.post.id = :postId")
    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);
}
