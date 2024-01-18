package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.dto.domain.UserFollowersDTO;
import com.me.social.dto.request.FollowRequestDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserUtil userUtil;

    private final UserRepository userRepository;

    @Value("${app.users.maxPageSize}")
    private int maxUserPageSize;

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
    public DefaultApiResponse<?> updateInformation(UserUpdateRequestDTO requestDTO, Long id, long loggedInUserId) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            if(id!=loggedInUserId){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            Optional<User> optionalUser = userRepository.findByIdAndDeleted(id,false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            Optional<User> existingUser = userRepository.findByEmail(requestDTO.getEmail());
            if(existingUser.isPresent()){
                User user = existingUser.get();
                if(user.getId()!=loggedInUserId){
                    apiResponse.setStatus(ExtendedConstants.ResponseCode.ALREADY_IN_USE.getStatus());
                    apiResponse.setMessage(String.format(ExtendedConstants.ResponseCode.ALREADY_IN_USE.getMessage(),"Email"));
                    return apiResponse;
                }
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
    public DefaultApiResponse<?> deactivate(Long id, long loggedInUserId) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            if(id!=loggedInUserId){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

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

    @Override
    public DefaultApiResponse<?> follow(FollowRequestDTO requestDTO) {
        DefaultApiResponse<Object> apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Long followerId = requestDTO.getFollowerId();
            Long followingId = requestDTO.getFollowingId();
            if(followerId.equals(followingId)){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_ACTION.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_ACTION.getMessage());
                return apiResponse;
            }

            Optional<User> optionalFollowerUser = userRepository.findByIdAndDeleted(followerId,false);
            if(optionalFollowerUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }
            User followerUser = optionalFollowerUser.get();

            Optional<User> optionalFollowingUser = userRepository.findByIdAndDeleted(followingId,false);
            if(optionalFollowingUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.USER_NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.USER_NOT_FOUND.getMessage());
                return apiResponse;
            }

            User followingUser = optionalFollowingUser.get();
//            List<UserFollowersDTO> userFollowers = userRepository.findUserFollowers(followingUser.getId());
            boolean alreadyFollowing = userRepository.isAlreadyFollowing(followerId, followingId);
            long followersCount = followingUser.getFollowersCount();
            if(alreadyFollowing){
                unfollowUser(followingUser, followerUser);
            } else{
                followUser(followingUser, followerUser);
            }

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    public void followUser(User following,User follower) {
        follower.getFollowing().add(following);
        following.setFollowersCount(following.getFollowersCount() + 1);
        userRepository.saveAll(List.of(following,follower));
    }

    public void unfollowUser(User following,User follower) {
        follower.getFollowing().remove(following);
        following.setFollowersCount(following.getFollowersCount() - 1);
        userRepository.saveAll(List.of(following,follower));
    }
}
