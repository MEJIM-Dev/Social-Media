package com.me.social.service;

import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.request.auth.AuthenticationRequestDTO;
import com.me.social.dto.response.DefaultApiResponse;

public interface AuthenticationService {

    DefaultApiResponse<?> registration(RegistrationDTO registrationDTO);

    DefaultApiResponse<?> login(AuthenticationRequestDTO requestDTO);
}
