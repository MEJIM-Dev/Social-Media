package com.me.social.util;

import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserUtil {
    public User dtoToUser(RegistrationDTO registrationDTO) {
        User user = new User();
        user.setActive(true);
        user.setEmail(registrationDTO.getEmail());
        user.setUsername(registrationDTO.getUsername());
        user.setSex(registrationDTO.getSex());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setOtherNames(registrationDTO.getOtherNames());
        user.setPassword(registrationDTO.getPassword());
        user.setProfilePicture(registrationDTO.getProfilePicture());
        user.setCreatedAt(Instant.now());
        return user;
    }

    public UserResponseDTO userToResponseDto (User user){
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setCreatedAt(user.getCreatedAt());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setSex(user.getSex());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setOtherNames(user.getOtherNames());
        responseDTO.setProfilePicture(user.getProfilePicture());
        responseDTO.setId(user.getId());
        return responseDTO;
    }
}
