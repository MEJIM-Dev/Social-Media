package com.me.social.controller;

import com.me.social.config.ApplicationUrl;
import com.me.social.dto.domain.UserDTO;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.request.UserUpdateRequestDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.service.UserService;
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
public class UserController {

    private final UserService userService;

    private final UserUtil userUtil;

    @GetMapping(ApplicationUrl.USERS)
    public DefaultApiResponse<?> getUsers(Pageable pageable){
        log.info("[+] Inside UserController.getUsers with page: {}",pageable);
        return userService.getUsers(pageable);
    }

    @GetMapping(ApplicationUrl.USER)
    public DefaultApiResponse<?> getUser(@PathVariable Long id){
        log.info("[+] Inside UserController.getUsers with page: {}",id);
        return userService.getUser(id);
    }

    @PutMapping(ApplicationUrl.USER)
    public DefaultApiResponse<?> updateInformation(@Valid @RequestBody UserUpdateRequestDTO requestDTO,@PathVariable Long id){
        log.info("[+] Inside UserController.updateInformation with dto: {}, id: {}",requestDTO, id);
        UserDTO loggedInUser = userUtil.getLoggedInUserProfile();
        return userService.updateInformation(requestDTO,id,loggedInUser.getId());
    }

    @DeleteMapping(ApplicationUrl.USER)
    public DefaultApiResponse<?> deactivate(@PathVariable Long id){
        log.info("[+] Inside UserController.deactivate id: {}", id);
        UserDTO loggedInUser = userUtil.getLoggedInUserProfile();
        return userService.deactivate(id,loggedInUser.getId());
    }

}
