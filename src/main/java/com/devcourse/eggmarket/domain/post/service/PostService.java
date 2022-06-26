package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import com.devcourse.eggmarket.domain.user.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface PostService {

    Long save(Save request, String loginUser);

    Long updatePost(Long id, PostRequest.UpdatePost request, String loginUser);

    Long updatePurchaseInfo(PostRequest.UpdatePurchaseInfo request);

    void deleteById(Long id, String loginUser);

    PostResponse getById(Long id);

    List<PostResponse> getAll(Pageable pageable);

    List<PostResponse> getAllByCategory(Pageable pageable, String category);
}
