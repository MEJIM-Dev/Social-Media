package com.me.social.service;

import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.request.UserUpdateRequestDTO;
import com.me.social.dto.response.DefaultApiResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    DefaultApiResponse<?> getUsers(Pageable pageable);

    DefaultApiResponse<?> getUser(Long id);

    DefaultApiResponse<?> updateInformation(UserUpdateRequestDTO requestDTO, Long id);

    DefaultApiResponse<?> deactivate(Long id);
}
