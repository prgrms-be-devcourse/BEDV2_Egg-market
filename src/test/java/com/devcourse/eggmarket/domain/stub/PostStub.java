package com.devcourse.eggmarket.domain.stub;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePost;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.user.model.User;

public class PostStub {

    private PostStub() {

    }

    public static PostRequest.Save writeRequest() {
        return new PostRequest.Save(
            "title",
            "content",
            1000,
            "BEAUTY"
        );
    }

    public static PostRequest.Save invalidCategoryWriteRequest() {
        return new PostRequest.Save(
            "title",
            "content",
            1000,
            "FOOD"
        );
    }

    public static Post entity(User seller) {
        return Post.builder()
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
}

