package com.devcourse.eggmarket.domain.post.api;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.service.PostService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    ResponseEntity<Long> write(@RequestBody PostRequest.Save request,
        Authentication authentication) {
        Long postId = postService.save(request, authentication.getName());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(postId)
            .toUri();

        return ResponseEntity.created(location)
            .body(postId);
    }

    @PatchMapping("{id}")
    ResponseEntity<Long> update(@RequestBody PostRequest.UpdatePost request,
        Authentication authentication,
        @PathVariable Long id) {
        Long postId = postService.updatePost(id, request, authentication.getName());

        return ResponseEntity.ok(postId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        postService.deleteById(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
