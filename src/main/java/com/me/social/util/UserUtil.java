package com.me.social.util;

import com.google.gson.Gson;
import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.dto.domain.UserDTO;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.UserResponseDTO;
import com.me.social.dto.security.JwtDecodedDTO;
import com.me.social.exception.AppUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
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
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        extraClaims.put("otherNames", user.getOtherNames());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("profilePicture", user.getProfilePicture());
        extraClaims.put("id", user.getId());
        return extraClaims;
    }
    
    public UserDTO getLoggedInUserProfile() {
        Optional<String> currentUserJWT = SecurityUtil.getCurrentUserJWT();
        if (currentUserJWT.isEmpty()) {
            throw new AppUserException(ExtendedConstants.ResponseCode.UNAUTHORIZED.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return getUserDtoFromToken(currentUserJWT.get());
    }

    private UserDTO getUserDtoFromToken(String jwtToken) {
        log.info("getLoggedInUserProfile {}", jwtToken);
        Base64 base64Url = new Base64(true);
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];
        String body = new String(base64Url.decode(base64EncodedBody));
        JwtDecodedDTO jwtDecodeDto = new Gson().fromJson(body, JwtDecodedDTO.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(jwtDecodeDto.getId());
        userDTO.setEmail(jwtDecodeDto.getEmail());
        userDTO.setFirstName(jwtDecodeDto.getFirstName());
        userDTO.setLastName(jwtDecodeDto.getLastName());
        userDTO.setSex(jwtDecodeDto.getSex());
        userDTO.setProfilePicture(jwtDecodeDto.getProfilePicture());
        userDTO.setOtherNames(jwtDecodeDto.getOtherNames());
        userDTO.setUsername(jwtDecodeDto.getUsername());
        return userDTO;
    }
}
