package com.me.social.service;

import com.me.social.dto.request.CommentRequest;
import com.me.social.dto.request.CommentUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    DefaultApiResponse<?> create(CommentRequest commentRequest);

    DefaultApiResponse<?> getAll(Pageable pageable);

    DefaultApiResponse<?> getById(Long id);

    DefaultApiResponse<?> update(Long id, CommentUpdateDTO userId);

    DefaultApiResponse<?> remove(Long id, long userId);
}
