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
import java.util.List;

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
            "test"
        );
    }

    public static SinglePost singlePostResponse(Post post) {
        User seller = post.getSeller();
        return new PostResponse.SinglePost(
            post.getId(),
            new UserResponse(
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
            post.getCreatedAt(),
            post.getAttentionCount(),
            0,
            true,
            null
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
                    0,
                    0,
                    null
                ),
                new PostsElement(
                    2L,
                    500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
                ),
                new PostsElement(
                    1L,
                    2500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
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
                    null
                ),
                new PostsElement(
                    1L,
                    1000,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
                ),
                new PostsElement(
                    3L,
                    2500,
                    "title",
                    "COMPLETED",
                    LocalDateTime.now(),
                    0,
                    0,
                    null
                )
            )
        );
    }
}

