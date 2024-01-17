package com.me.social.controller;

import com.me.social.config.ApplicationUrl;
import com.me.social.dto.request.CreatePostDTO;
import com.me.social.dto.request.PostUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.service.PostService;
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

    @PostMapping(ApplicationUrl.POSTS)
    public DefaultApiResponse<?> create(CreatePostDTO postDto){
        log.debug("[+] Inside PostController.create with dto: {}",postDto);
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
    public DefaultApiResponse<?> getUserPosts(@PathVariable Long userId,Pageable pageable){
        log.debug("[+] Inside PostController.getUserPosts with userId: {}, with page: {}",userId,pageable);
        return postService.getUserPosts(userId,pageable);
    }

    @PutMapping(ApplicationUrl.POST)
    public DefaultApiResponse<?> update(@PathVariable Long id, PostUpdateDTO updateDTO){
        log.debug("[+] Inside PostController.update with id: {}, with dto: {}",id,updateDTO);
        return postService.update(id,updateDTO);
    }

    @DeleteMapping(ApplicationUrl.POST)
    public DefaultApiResponse<?> remove(@PathVariable Long id){
        log.debug("[+] Inside PostController.remove with id: {}",id);
        return postService.remove(id);
    }
}
