package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.dto.request.CreatePostDTO;
import com.me.social.dto.request.PostUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.dto.response.PostResponseDTO;
import com.me.social.repository.PostRepository;
import com.me.social.repository.UserRepository;
import com.me.social.service.PostService;
import com.me.social.util.PostUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Value("${app.posts.maxPageSize}")
    private int maxPageSize;

    private final PostUtil postUtil;

    private final UserRepository userRepository;

    @Override
    public DefaultApiResponse<?> create(CreatePostDTO postDto) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Post post = new Post();
            post.setContent(postDto.getContent());
            post.setCreationDate(Instant.now());
            post.setLikesCount(0);
            //Get Logged in user
            //Add to Post
            postRepository.save(post);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getAll(Pageable pageable) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            int pageSize = pageable.getPageSize()>maxPageSize ? maxPageSize : pageable.getPageSize();
            PageRequest page = PageRequest.of(pageable.getPageNumber(), pageSize,pageable.getSort());
            Page<Post> posts = postRepository.findAll(page);

            Map<String,Object> pagedResponse = new HashMap<>();
            pagedResponse.put("totalPage",posts.getTotalPages());
            pagedResponse.put("totalContent",posts.getTotalElements());
            List<PostResponseDTO> data = posts.getContent().stream().map(post -> {
                return postUtil.postToDto(post);
            }).toList();
            pagedResponse.put("content",data);

            apiResponse.setData(pagedResponse);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getById(Long id) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Post> optionalPost = postRepository.findById(id);

            if(optionalPost.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            apiResponse.setData(postUtil.postToDto(optionalPost.get()));
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getUserPosts(Long userId, Pageable pageable) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(userId,false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            User user = optionalUser.get();
            int pageSize = pageable.getPageSize()>maxPageSize ? maxPageSize : pageable.getPageSize();
            PageRequest page = PageRequest.of(pageable.getPageNumber(), pageSize,pageable.getSort());
            Page<Post> posts = postRepository.findByUser(user,page);

            Map<String,Object> pagedResponse = new HashMap<>();
            pagedResponse.put("totalPage",posts.getTotalPages());
            pagedResponse.put("totalContent",posts.getTotalElements());
            List<PostResponseDTO> data = posts.getContent().stream().map(post -> {
                return postUtil.postToDto(post);
            }).toList();
            pagedResponse.put("content",data);

            apiResponse.setData(pagedResponse);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> update(Long id, PostUpdateDTO updateDTO) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Post> optionalPost = postRepository.findByIdAndDeleted(id, false);
            if(optionalPost.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_POST.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_POST.getMessage());
                return apiResponse;
            }

            Post post = optionalPost.get();
            post.setContent(updateDTO.getContent());
            postRepository.save(post);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> remove(Long id) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Post> optionalPost = postRepository.findByIdAndDeleted(id, false);
            if(optionalPost.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_POST.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_POST.getMessage());
                return apiResponse;
            }

            Post post = optionalPost.get();
            post.setDeleted(true);
            postRepository.save(post);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

}
