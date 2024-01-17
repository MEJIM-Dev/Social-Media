package com.me.social.util;

import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserUtil {

    private final PasswordEncoder passwordEncoder;

    public User dtoToUser(RegistrationDTO registrationDTO) {
        User user = new User();
        user.setActive(true);
        user.setEmail(registrationDTO.getEmail());
        user.setUsername(registrationDTO.getUsername());
        user.setSex(registrationDTO.getSex());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setOtherNames(registrationDTO.getOtherNames());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
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

    public Map<String, Object> getExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());
        extraClaims.put("sex", user.getSex());
        extraClaims.put("firstname", user.getFirstName());
        extraClaims.put("lastname", user.getLastName());
        extraClaims.put("otherNames", user.getOtherNames());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("profilePicture", user.getProfilePicture());
        return extraClaims;
    }
}
