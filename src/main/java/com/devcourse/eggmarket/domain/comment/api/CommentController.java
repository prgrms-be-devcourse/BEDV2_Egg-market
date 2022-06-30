package com.devcourse.eggmarket.domain.comment.api;

import com.devcourse.eggmarket.domain.comment.dto.CommentRequest;
import com.devcourse.eggmarket.domain.comment.service.CommentService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(
        CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> save(@PathVariable Long postId,
        @RequestBody @Valid CommentRequest.Save createRequest, Authentication authentication) {

        Long commentId = commentService.write(authentication.getName(), postId, createRequest);

        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/post/{postId}/comments/{commentId}")
            .buildAndExpand(postId, commentId)
            .toUri();

        return ResponseEntity.created(location)
            .body(commentId);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Long> update(@PathVariable Long postId,
        @PathVariable Long commentId, @RequestBody @Valid CommentRequest.Update updateRequest,
        Authentication authentication) {

        Long updatedCommentId = commentService.update(authentication.getName(), postId, commentId,
            updateRequest);

        return ResponseEntity.ok(updatedCommentId);
    }
}
