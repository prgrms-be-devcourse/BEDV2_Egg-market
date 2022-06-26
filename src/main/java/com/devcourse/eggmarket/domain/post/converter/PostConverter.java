package com.devcourse.eggmarket.domain.post.converter;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public Post saveToPost(PostRequest.Save request, User seller) {
        return Post.builder()
            .title(request.title())
            .content(request.content())
            .category(Category.valueOf(request.category()))
            .price(request.price())
            .seller(seller)
            .build();
    }

    public void updateToPost(PostRequest.UpdatePost request, Post post) {
        post.updateTitle(request.title());
        post.updateContent(request.content());
        post.updatePrice(request.price());
        post.updateCategory(Category.valueOf(request.category()));
    }

    public void updateToPurchase(UpdatePurchaseInfo request, Post post,
        User buyer) {
        post.updatePurchaseInfo(
            PostStatus.valueOf(request.postStatus()),
            buyer
        );
    }
}
