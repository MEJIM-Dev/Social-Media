package com.me.social.util;

import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {
    public User dtoToUser(RegistrationDTO registrationDTO) {
        User user = new User();
        return user;
    }
}
