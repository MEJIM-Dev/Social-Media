package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.Comment;
import com.me.social.domain.Post;
import com.me.social.domain.PostReaction;
import com.me.social.domain.User;
import com.me.social.dto.request.CreatePostDTO;
import com.me.social.dto.request.PostUpdateDTO;
import com.me.social.dto.response.CommentResponseDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.dto.response.PostResponseDTO;
import com.me.social.repository.CommentRepository;
import com.me.social.repository.PostReactionRepository;
import com.me.social.repository.PostRepository;
import com.me.social.repository.UserRepository;
import com.me.social.service.PostService;
import com.me.social.util.CommentUtil;
import com.me.social.util.PostUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Value("${app.posts.maxPageSize}")
    private int maxPageSize;

    private final PostUtil postUtil;

    private final UserRepository userRepository;

    private final PostReactionRepository reactionRepository;

    @Value("${app.comments.maxPageSize}")
    private int commentsMaxPageSize;

    private final CommentRepository commentRepository;

    private final CommentUtil commentUtil;

    @Override
    public DefaultApiResponse<?> create(CreatePostDTO postDto) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(postDto.getUserId(), false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            Post post = new Post();
            post.setContent(postDto.getContent());
            post.setCreationDate(Instant.now());
            post.setLikesCount(0);
            post.setUser(optionalUser.get());
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
            if(post.getUser()==null || post.getUser().getId()!=updateDTO.getUserId()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

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
    public DefaultApiResponse<?> remove(Long id, long loggedInUserProfileId) {
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

            if(post.getUser()==null || post.getUser().getId()!=loggedInUserProfileId){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            post.setDeleted(true);
            postRepository.save(post);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> reactToPost(Long id, long userId) {
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
            long likesCount = post.getLikesCount();

            Optional<User> optionalUser = userRepository.findByIdAndDeleted(userId, false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }
            User user = optionalUser.get();

            Optional<PostReaction> byPostAndUser = reactionRepository.findByPostAndUser(post, user);
            if(byPostAndUser.isEmpty()){
                PostReaction postReaction = new PostReaction();
                postReaction.setPost(post);
                postReaction.setUser(user);
                reactionRepository.save(postReaction);
                likesCount++;
            } else {
                likesCount--;
                reactionRepository.delete(byPostAndUser.get());
            }

            post.setLikesCount(likesCount);
            postRepository.save(post);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> getComments(Long id, Pageable pageable) {
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
            int pageSize = pageable.getPageSize()>commentsMaxPageSize ? commentsMaxPageSize : pageable.getPageSize();
            PageRequest page = PageRequest.of(pageable.getPageNumber(), pageSize,pageable.getSort());
            Page<Comment> comments = commentRepository.findByPost(post,page);

            Map<String,Object> pagedResponse = new HashMap<>();
            pagedResponse.put("totalPage",comments.getTotalPages());
            pagedResponse.put("totalContent",comments.getTotalElements());
            List<CommentResponseDTO> data = comments.getContent().stream().map(commentUtil::commentToDto).toList();
            pagedResponse.put("content",data);

            apiResponse.setData(pagedResponse);
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

}
