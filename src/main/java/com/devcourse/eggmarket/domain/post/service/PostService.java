package com.devcourse.eggmarket.domain.post.service;

import com.devcourse.eggmarket.domain.post.dto.PostRequest;
import com.devcourse.eggmarket.domain.post.dto.PostRequest.Save;
import com.devcourse.eggmarket.domain.post.dto.PostResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {

  Long save(Save request);

  Long updatePost(Long id, PostRequest.UpdatePost request);

  Long updatePurchaseInfo(PostRequest.UpdatePurchaseInfo request);

  boolean deleteById(Long id);

  PostResponse getById(Long id);

  List<PostResponse> getAll(Pageable pageable);

  List<PostResponse> getAllByCategory(Pageable pageable, String category);
}