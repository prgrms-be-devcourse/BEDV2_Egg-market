package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Long save(Save request, String loginUser);

    Long updatePost(Long id, PostRequest.UpdatePost request, String loginUser);

    Long updatePurchaseInfo(Long id, PostRequest.UpdatePurchaseInfo request, String loginUser);

    void deleteById(Long id, String loginUser);

    PostResponse.SinglePost getById(Long id);

    List<PostResponse.SinglePost> getAll(Pageable pageable);

    List<PostResponse.SinglePost> getAllByCategory(Pageable pageable, String category);
}
