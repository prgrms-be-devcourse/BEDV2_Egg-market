package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.Posts;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostsElement;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.SinglePost;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class PostStub {

    private PostStub() {

    }

    public static PostRequest.Save writeRequest() {
        return new PostRequest.Save(
            "title",
            "content",
            1000,
            "BEAUTY",
            null
        );
    }

    public static PostRequest.Save invalidCategoryWriteRequest() {
        return new PostRequest.Save(
            "title",
            "content",
            1000,
            "FOOD",
            null
        );
    }

    public static Post entity(User seller) {
        return Post.builder()
            .id(1L)
            .title("title")
            .content("content")
            .price(1000)
            .category(Category.BEAUTY)
            .seller(seller)
            .build();
    }

    public static PostRequest.UpdatePost updatePostRequest() {
        return new PostRequest.UpdatePost(
            "update-title",
            "update-content",
            2000,
            "DIGITAL"
        );
    }

    public static Post updatedEntity(User seller) {
        return Post.builder()
            .title("update-title")
            .content("update-content")
            .price(2000)
            .category(Category.DIGITAL)
            .seller(seller)
            .build();
    }

    public static PostRequest.UpdatePurchaseInfo updatePurchaseInfo() {
        return new PostRequest.UpdatePurchaseInfo(
            "COMPLETED",
            2L
        );
    }

    public static SinglePost singlePostResponse(Post post) {
        User seller = post.getSeller();
        return new PostResponse.SinglePost(
            post.getId(),
            new UserResponse.Basic(
                seller.getId(),
                seller.getNickName(),
                seller.getMannerTemperature(),
                seller.getRole().name(),
                seller.getImagePath()
            ),
            post.getPrice(),
            post.getTitle(),
            post.getContent(),
            post.getPostStatus().name(),
            post.getCategory().name(),
            post.getCreatedAt() == null ? LocalDateTime.now() : post.getCreatedAt(),
            1,
            1,
            true,
            List.of(ImageStub.uploadImage1(post.getId()).pathTobeStored(""),
                ImageStub.uploadImage2(post.getId()).pathTobeStored(""))
        );
    }

    public static SinglePost noDependencySinglePostResponse(Post post) {
        User seller = post.getSeller();
        return new PostResponse.SinglePost(
            post.getId(),
            new UserResponse.Basic(
                seller.getId(),
                seller.getNickName(),
                seller.getMannerTemperature(),
                seller.getRole().name(),
                seller.getImagePath()
            ),
            post.getPrice(),
            post.getTitle(),
            post.getContent(),
            post.getPostStatus().name(),
            post.getCategory().name(),
            post.getCreatedAt() == null ? LocalDateTime.now() : post.getCreatedAt(),
            0,
            0,
            false,
            new ArrayList<>()
        );
    }

    public static PostResponse.Posts posts(Post post1, Post post2, Post post3) {
        return new PostResponse.Posts(
            List.of(
                new PostsElement(
                    post1.getId(),
                    post1.getPrice(),
                    post1.getTitle(),
                    post1.getPostStatus().toString(),
                    post1.getCreatedAt(),
                    post1.getAttentionCount(),
                    post1.getCommentCount(),
                    ""
                ),
                new PostsElement(
                    post2.getId(),
                    post2.getPrice(),
                    post2.getTitle(),
                    post2.getPostStatus().toString(),
                    post2.getCreatedAt(),
                    post2.getAttentionCount(),
                    post2.getCommentCount(),
                    ""
                ),
                new PostsElement(
                    post3.getId(),
                    post3.getPrice(),
                    post3.getTitle(),
                    post3.getPostStatus().toString(),
                    post3.getCreatedAt(),
                    post3.getAttentionCount(),
                    post3.getCommentCount(),
                    ""
                )
            )
        );
    }

    public static PostResponse.Posts posts(Post post1) {
        return new PostResponse.Posts(
            List.of(
                new PostsElement(
                    post1.getId(),
                    post1.getPrice(),
                    post1.getTitle(),
                    post1.getPostStatus().toString(),
                    post1.getCreatedAt(),
                    post1.getAttentionCount(),
                    post1.getCommentCount(),
                    ""
                )
            )
        );
    }

    public static PostResponse.Posts posts() {
        return new PostResponse.Posts(
            List.of(
                new PostsElement(
                    1L,
                    1000,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    3,
                    2,
                    "http://example.com/test/1-1.png"
                ),
                new PostsElement(
                    2L,
                    500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    1,
                    0,
                    "http://example.com/test/2-1.png"
                ),
                new PostsElement(
                    3L,
                    2500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    5,
                    12,
                    "http://example.com/test/3-1.png"
                )
            )
        );
    }

    public static Posts priceSortPosts() {
        return new PostResponse.Posts(
            List.of(
                new PostsElement(
                    2L,
                    500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    "http://example.com/test/2-1.png"
                ),
                new PostsElement(
                    1L,
                    1000,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    "http://example.com/test/1-1.png"
                ),
                new PostsElement(
                    3L,
                    2500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    "http://example.com/test/3-1.png"
                )
            )
        );
    }

    public static PostResponse.Posts posts(Post post1, Post post2) {
        return new PostResponse.Posts(
            List.of(
                new PostsElement(
                    post1.getId(),
                    post1.getPrice(),
                    post1.getTitle(),
                    post1.getPostStatus().toString(),
                    post1.getCreatedAt(),
                    post1.getAttentionCount(),
                    post1.getCommentCount(),
                    ""
                ),
                new PostsElement(
                    post2.getId(),
                    post2.getPrice(),
                    post2.getTitle(),
                    post2.getPostStatus().toString(),
                    post2.getCreatedAt(),
                    post2.getAttentionCount(),
                    post2.getCommentCount(),
                    ""
                )
            )
        );
    }
    public static Posts searchPosts(Long id1, Long id2) {
        return new Posts(
            List.of(
                new PostsElement(
                    id1,
                    1000,
                    "test1",
                    "SALE",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
                ),
                new PostsElement(
                    id2,
                    2500,
                    "test2",
                    "SALE",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
                )
            )
        );
    }

    public static Stream<Arguments> invalidWriteRequest() {
        return Stream.of(
            Arguments.arguments(
                new PostRequest.Save("", "content", 1000, "BEAUTY", null)
            ),
            Arguments.arguments(
                new PostRequest.Save("title", "", 1000, "BEAUTY", null)
            ),
            Arguments.arguments(
                new PostRequest.Save("title", "content", -1000, "BEAUTY", null)
            ),
            Arguments.arguments(
                new PostRequest.Save("title", "content", 1000, "INVALID", null)
            )
        );
    }

    public static Stream<Arguments> invalidUpdatePostRequest() {
        return Stream.of(
            Arguments.arguments(
                new PostRequest.UpdatePost("", "content", 1000, "BEAUTY")
            ),
            Arguments.arguments(
                new PostRequest.UpdatePost("title", "", 1000, "BEAUTY")
            ),
            Arguments.arguments(
                new PostRequest.UpdatePost("title", "content", -1000, "BEAUTY")
            ),
            Arguments.arguments(
                new PostRequest.UpdatePost("title", "content", 1000, "INVALID")
            )
        );
    }

    public static Stream<Arguments> invalidUpdatePurchaseInfoRequest() {
        return Stream.of(
            Arguments.arguments(
                new PostRequest.UpdatePurchaseInfo("COMPLTED", 0L)
            ),
            Arguments.arguments(
                new PostRequest.UpdatePurchaseInfo("INVALID", 1L)
            )
        );
    }

}

