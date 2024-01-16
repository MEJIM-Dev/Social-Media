package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.repository.UserRepository;
import com.me.social.service.UserService;
import com.me.social.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserUtil userUtil;

    private final UserRepository userRepository;

    @Override
    public DefaultApiResponse<?> registration(RegistrationDTO registrationDTO) {
        DefaultApiResponse<?> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            User user = userUtil.dtoToUser(registrationDTO);
            userRepository.save(user);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESSFUL.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESSFUL.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }
}
