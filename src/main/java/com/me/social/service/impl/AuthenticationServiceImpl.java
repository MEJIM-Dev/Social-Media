package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.request.auth.AuthenticationRequestDTO;
import com.me.social.dto.response.AuthenticationToken;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.repository.PostRepository;
import com.me.social.repository.UserRepository;
import com.me.social.service.AuthenticationService;
import com.me.social.service.JwtService;
import com.me.social.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserUtil userUtil;

    private final JwtService jwtService;


    private AuthenticationToken authenticateUser(String password, User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtService.generateJwt(user,userUtil.getExtraClaims(user));
        AuthenticationToken responseToken = new AuthenticationToken();
        responseToken.setAccess_token(accessToken);

        return responseToken;
    }

    @Override
    public DefaultApiResponse<?> registration(RegistrationDTO registrationDTO) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            List<User> existingUsers = userRepository.findByUsernameOrEmail(registrationDTO.getUsername(), registrationDTO.getEmail());
            if(!existingUsers.isEmpty()){
                apiResponse.setMessage(ExtendedConstants.ResponseCode.DUPLICATE_RECORD.getMessage());
                return apiResponse;
            }

            User user = userUtil.dtoToUser(registrationDTO);
            user = userRepository.save(user);

            AuthenticationToken authenticationToken = authenticateUser(registrationDTO.getPassword(), user);

            apiResponse.setData(authenticationToken);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> login(AuthenticationRequestDTO requestDTO) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(requestDTO.getUsername(), false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            User user = optionalUser.get();
            AuthenticationToken authenticationToken = authenticateUser(requestDTO.getPassword(), user);

            apiResponse.setData(authenticationToken);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

}
