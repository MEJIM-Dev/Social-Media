package com.me.social.controller;

import com.me.social.config.ApplicationUrl;
import com.me.social.dto.domain.UserDTO;
import com.me.social.dto.request.CommentRequest;
import com.me.social.dto.request.CommentUpdateDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.service.CommentService;
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
public class CommentController {

    private final UserUtil userUtil;

    private final CommentService commentService;

    @PostMapping(ApplicationUrl.COMMENTS)
    public DefaultApiResponse<?> create(@Valid @RequestBody CommentRequest commentRequest){
        log.info("[+] Inside CommentController.create with dto: {}",commentRequest);
        UserDTO loggedInUser = userUtil.getLoggedInUserProfile();
        commentRequest.setUserId(loggedInUser.getId());
        return commentService.create(commentRequest);
    }

    @GetMapping(ApplicationUrl.COMMENTS)
    public DefaultApiResponse<?> getAll(Pageable pageable){
        log.info("[+] Inside CommentController.getAll with page: {}",pageable);
        return commentService.getAll(pageable);
    }

    @GetMapping(ApplicationUrl.COMMENT)
    public DefaultApiResponse<?> getById(@PathVariable Long id){
        log.info("[+] Inside CommentController.getById with id: {}",id);
        return commentService.getById(id);
    }

    @PutMapping(ApplicationUrl.COMMENT)
    public DefaultApiResponse<?> update(@Valid @RequestBody CommentUpdateDTO updateDTO, @PathVariable Long id){
        log.info("[+] Inside CommentController.update with id: {}, dto: {}",id,updateDTO);
        UserDTO loggedInUser = userUtil.getLoggedInUserProfile();
        updateDTO.setUserId(loggedInUser.getId());
        return commentService.update(id,updateDTO);
    }

    @DeleteMapping(ApplicationUrl.COMMENT)
    public DefaultApiResponse<?> remove(@PathVariable Long id){
        log.info("[+] Inside CommentController.remove with id: {}",id);
        UserDTO loggedInUser = userUtil.getLoggedInUserProfile();
        return commentService.remove(id,loggedInUser.getId());
    }
}
