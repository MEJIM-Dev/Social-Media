package com.me.social.controller;

import com.me.social.config.ApplicationUrl;
import com.me.social.dto.request.RegistrationDTO;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApplicationUrl.BASE_URL)
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApplicationUrl.REGISTRATION)
    public DefaultApiResponse<?> registration(@Valid @RequestBody RegistrationDTO registrationDTO){
        log.info("[+] Inside UserController.registration with dto: {}",registrationDTO);
        return userService.registration(registrationDTO);
    }
}
