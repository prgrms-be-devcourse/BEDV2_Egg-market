package com.devcourse.eggmarket.domain.post.api;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostAttentionCount;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.service.PostAttentionService;
import com.devcourse.eggmarket.domain.post.service.PostService;
import com.devcourse.eggmarket.global.common.SuccessResponse;
import com.devcourse.eggmarket.global.common.ValueOfEnum;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Validated
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostAttentionService postAttentionService;

    public PostController(PostService postService,
        PostAttentionService postAttentionService) {
        this.postService = postService;
        this.postAttentionService = postAttentionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Long>> write(@Valid PostRequest.Save request,
        Authentication authentication) {
        Long response = postService.save(request, authentication.getName());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response)
            .toUri();

        return ResponseEntity.created(location)
            .body(new SuccessResponse<>(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<Long>> updatePost(
        @RequestBody @Valid PostRequest.UpdatePost request,
        Authentication authentication,
        @PathVariable Long id) {
        Long response = postService.updatePost(id, request, authentication.getName());

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("/{id}/purchase")
    public ResponseEntity<SuccessResponse<Long>> updatePurchase(
        @RequestBody @Valid PostRequest.UpdatePurchaseInfo request,
        Authentication authentication,
        @PathVariable Long id) {
        Long response = postService.updatePurchaseInfo(id, request, authentication.getName());

        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        postService.deleteById(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attention")
    public ResponseEntity<SuccessResponse<PostAttentionCount>> attention(@PathVariable("id") Long postId,
        Authentication authentication) {
        return ResponseEntity.ok(
            new SuccessResponse<>(
                postAttentionService.toggleAttention(authentication.getName(), postId)
            )
        );
    }

    @GetMapping("/attention")
    public ResponseEntity<SuccessResponse<Posts>> allAttention(Authentication authentication) {
        return ResponseEntity.ok(
            new SuccessResponse<>(
                postService.getAllLikedBy(authentication.getName())
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse.SinglePost>> getPost(@PathVariable Long id,
        Authentication authentication) {
        PostResponse.SinglePost response = postService.getById(id, authentication.getName());
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PostResponse.Posts>> getPosts(Pageable pageable,
        @RequestParam(required = false) @ValueOfEnum(enumClass = Category.class) String category) {
        return ResponseEntity.ok(
            new SuccessResponse<>(
                category != null ?
                    postService.getAllByCategory(pageable, Category.valueOf(category)) :
                    postService.getAll(pageable)
            )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<PostResponse.Posts>> search(Pageable pageable,
        @RequestParam @NotBlank(message = "검색할 내용을 입력해주세요") String word) {
        return ResponseEntity.ok(
            new SuccessResponse<>(
                postService.search(pageable, word)
            )
        );
    }
}
