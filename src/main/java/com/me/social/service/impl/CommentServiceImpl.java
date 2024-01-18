package com.me.social.service.impl;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.Comment;
import com.me.social.domain.Post;
import com.me.social.domain.User;
import com.me.social.dto.request.CommentRequest;
import com.me.social.dto.request.CommentUpdateDTO;
import com.me.social.dto.response.CommentResponseDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.repository.CommentRepository;
import com.me.social.repository.PostRepository;
import com.me.social.repository.UserRepository;
import com.me.social.service.CommentService;
import com.me.social.util.CommentUtil;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Value("${app.comments.maxPageSize}")
    private int maxPageSize;

    private final CommentUtil commentUtil;

    @Override
    public DefaultApiResponse<?> create(CommentRequest commentRequest) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<User> optionalUser = userRepository.findByIdAndDeleted(commentRequest.getUserId(), false);
            if(optionalUser.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            Optional<Post> optionalPost = postRepository.findByIdAndDeleted(commentRequest.getPostId(), false);
            if(optionalPost.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_POST.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_POST.getMessage());
                return apiResponse;
            }

            Comment comment = new Comment();
            comment.setContent(commentRequest.getComment());
            comment.setDeleted(false);
            comment.setUser(optionalUser.get());
            comment.setPost(optionalPost.get());
            comment.setCreationDate(Instant.now());
            commentRepository.save(comment);

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
            Page<Comment> comments = commentRepository.findAll(page);

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

    @Override
    public DefaultApiResponse<?> getById(Long id) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Comment> optionalComment = commentRepository.findByIdAndDeleted(id,false);

            if(optionalComment.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.NOT_FOUND.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.NOT_FOUND.getMessage());
                return apiResponse;
            }

            apiResponse.setData(commentUtil.commentToDto(optionalComment.get()));
            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> update(Long id, CommentUpdateDTO updateDTO) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Comment> optionalComment = commentRepository.findByIdAndDeleted(id, false);
            if(optionalComment.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_COMMENT.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_COMMENT.getMessage());
                return apiResponse;
            }

            Comment comment = optionalComment.get();
            if(comment.getUser()==null || comment.getUser().getId()!=updateDTO.getUserId()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            comment.setContent(updateDTO.getContent());
            commentRepository.save(comment);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }

    @Override
    public DefaultApiResponse<?> remove(Long id, long userId) {
        var apiResponse = new DefaultApiResponse<>();
        apiResponse.setStatus(ExtendedConstants.ResponseCode.FAIL.getStatus());
        apiResponse.setMessage(ExtendedConstants.ResponseCode.FAIL.getMessage());
        try {
            Optional<Comment> optionalComment = commentRepository.findByIdAndDeleted(id, false);
            if(optionalComment.isEmpty()){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_COMMENT.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_COMMENT.getMessage());
                return apiResponse;
            }

            Comment comment = optionalComment.get();

            if(comment.getUser()==null || comment.getUser().getId()!=userId){
                apiResponse.setStatus(ExtendedConstants.ResponseCode.INVALID_USER.getStatus());
                apiResponse.setMessage(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
                return apiResponse;
            }

            comment.setDeleted(true);
            commentRepository.save(comment);

            apiResponse.setStatus(ExtendedConstants.ResponseCode.SUCCESS.getStatus());
            apiResponse.setMessage(ExtendedConstants.ResponseCode.SUCCESS.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return apiResponse;
    }


}
