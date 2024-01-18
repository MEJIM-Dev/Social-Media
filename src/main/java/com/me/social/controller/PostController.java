package com.me.social.controller;

import com.me.social.config.ApplicationUrl;
import com.me.social.dto.domain.UserDTO;
import com.me.social.dto.request.CreatePostDTO;
import com.me.social.dto.request.PostUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.service.PostService;
import com.me.social.util.UserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApplicationUrl.BASE_URL)
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserUtil userUtil;

    @PostMapping(ApplicationUrl.POSTS)
    public DefaultApiResponse<?> create(@Valid @RequestBody CreatePostDTO postDto){
        log.debug("[+] Inside PostController.create with dto: {}",postDto);
        UserDTO loggedInUserProfile = userUtil.getLoggedInUserProfile();
        postDto.setUserId(loggedInUserProfile.getId());
        return postService.create(postDto);
    }

    @GetMapping(ApplicationUrl.POSTS)
    public DefaultApiResponse<?> getAll(Pageable pageable){
        log.debug("[+] Inside PostController.create with pageable: {}",pageable);
        return postService.getAll(pageable);
    }

    @GetMapping(ApplicationUrl.POST)
    public DefaultApiResponse<?> getById(@PathVariable Long id){
        log.debug("[+] Inside PostController.create with id: {}",id);
        return postService.getById(id);
    }

    @GetMapping(ApplicationUrl.USER_POSTS)
    public DefaultApiResponse<?> getUserPosts(Pageable pageable){
        log.debug("[+] Inside PostController.getUserPosts with page: {}",pageable);
        UserDTO loggedInUserProfile = userUtil.getLoggedInUserProfile();
        return postService.getUserPosts(loggedInUserProfile.getId(),pageable);
    }

    @PutMapping(ApplicationUrl.POST)
    public DefaultApiResponse<?> update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO updateDTO){
        log.debug("[+] Inside PostController.update with id: {}, with dto: {}",id,updateDTO);
        UserDTO loggedInUserProfile = userUtil.getLoggedInUserProfile();
        updateDTO.setUserId(loggedInUserProfile.getId());
        return postService.update(id,updateDTO);
    }

    @DeleteMapping(ApplicationUrl.POST)
    public DefaultApiResponse<?> remove(@PathVariable Long id){
        log.debug("[+] Inside PostController.remove with id: {}",id);
        UserDTO loggedInUserProfile = userUtil.getLoggedInUserProfile();
        return postService.remove(id,loggedInUserProfile.getId());
    }

    @PostMapping(ApplicationUrl.LIKE)
    public DefaultApiResponse<?> reactToPost(@PathVariable Long id){
        log.debug("[+] Inside PostController.reactToPost with id: {}",id);
        UserDTO loggedInUserProfile = userUtil.getLoggedInUserProfile();
        return postService.reactToPost(id,loggedInUserProfile.getId());
    }

    @GetMapping(ApplicationUrl.POST_COMMENTS)
    public DefaultApiResponse<?> getComments(@PathVariable Long id,Pageable pageable){
        log.debug("[+] Inside PostController.getComments with id: {}, page: {}",id,pageable);
        return postService.getComments(id,pageable);
    }
}
