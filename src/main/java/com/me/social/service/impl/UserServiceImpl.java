package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.request.UserUpdateRequestDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.dto.response.UserResponseDTO;
import com.me.social.repository.UserRepository;
import com.me.social.service.UserService;
import com.me.social.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserUtil userUtil;

    private final UserRepository userRepository;

    @Value("${app.users.maxPageSize}")
    private int maxUserPageSize;

    @Override
    public DefaultApiResponse<?> registration(RegistrationDTO registrationDTO) {
        DefaultApiResponse<?> apiResponse = new DefaultApiResponse<>();
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

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getUsers(Pageable pageable) {
        DefaultApiResponse<Map> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            int pageSize = pageable.getPageSize()>maxUserPageSize ? maxUserPageSize : pageable.getPageSize();
            PageRequest page = PageRequest.of(pageable.getPageNumber(), pageSize,pageable.getSort());
            Page<User> users = userRepository.findByDeleted(false,page);

            Map<String,Object> pagedResponse = new HashMap<>();
            pagedResponse.put("totalPage",users.getTotalPages());
            pagedResponse.put("totalContent",users.getTotalElements());
            List<UserResponseDTO> data = users.getContent().stream().map(user -> {
                return userUtil.userToResponseDto(user);
            }).toList();
            pagedResponse.put("content",data);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
            apiResponse.setData(pagedResponse);
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getUser(Long id) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            apiResponse.setData(userUtil.userToResponseDto(optionalUser.get()));
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> updateInformation(UserUpdateRequestDTO requestDTO, Long id) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            User user = optionalUser.get();
            BeanUtils.copyProperties(requestDTO,user);
            userRepository.save(user);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> deactivate(Long id) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            User user = optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }
}
