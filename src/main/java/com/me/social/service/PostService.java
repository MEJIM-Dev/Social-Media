package com.me.social.service;

import com.me.social.dto.request.CreatePostDTO;
import com.me.social.dto.request.PostUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {

    DefaultApiResponse<?> create(CreatePostDTO postDto);

    DefaultApiResponse<?> getAll(Pageable pageable);

    DefaultApiResponse<?> getById(Long id);

    DefaultApiResponse<?> getUserPosts(Long userId, Pageable pageable);

    DefaultApiResponse<?> update(Long id, PostUpdateDTO updateDTO);

    DefaultApiResponse<?> remove(Long id, long loggedInUserProfileId);
}
