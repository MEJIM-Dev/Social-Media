package com.me.social.service;

import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.DefaultApiResponse;

public interface UserService {
    DefaultApiResponse<?> registration(RegistrationDTO registrationDTO);
}
