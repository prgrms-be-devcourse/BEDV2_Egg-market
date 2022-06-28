package com.devcourse.eggmarket.domain.post.converter;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.UpdatePurchaseInfo;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.post.dto.PostResponse.PostsElement;
import com.devcourse.eggmarket.domain.post.model.Category;
import com.devcourse.eggmarket.domain.post.model.Post;
import com.devcourse.eggmarket.domain.post.model.PostStatus;
import com.devcourse.eggmarket.domain.user.dto.UserResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import java.util.List;
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

    public PostResponse.SinglePost singlePost(Post post, boolean likeOfMe, List<String> imgPaths) {
        User seller = post.getSeller();

        return new PostResponse.SinglePost(
            post.getId(),
            // TODO : UserConverter 사용 고려 (주의: 순환참조되지 않도록 -> 즉 PostConverter 에서 UserConverter 를 사용한다면 UserConverter 에서는 PostConverter 를 사용하면 안됩니다)
            new UserResponse(
                seller.getId(),
                seller.getNickName(),
                seller.getMannerTemperature(),
                seller.getRole().toString(),
                seller.getImagePath()),
            post.getPrice(),
            post.getTitle(),
            post.getContent(),
            post.getPostStatus().name(),
            post.getCategory().name(),
            post.getCreatedAt(),
            post.getAttentionCount(),
            post.getCommentCount(),
            likeOfMe,
            imgPaths
        );
    }

    public PostResponse.PostsElement postsElement(Post post, String imgPath) {
        return new PostsElement(
            post.getId(),
            post.getPrice(),
            post.getTitle(),
            post.getPostStatus().toString(),
            post.getCreatedAt(),
            post.getAttentionCount(),
            post.getCommentCount(),
            imgPath
        );

    }

    public PostResponse.PostsElement convertToPostsElement(Post post) {
        return new PostsElement(
            post.getId(),
            post.getPrice(),
            post.getTitle(),
            post.getPostStatus().toString(),
            post.getCreatedAt(),
            post.getAttentionCount(),
            0, // TODO : 커멘트 개수 post 에서 가져오기
            null
        );

    }

}
