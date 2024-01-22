package com.me.social.service;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.dto.response.UserResponseDTO;
import com.me.social.repository.UserRepository;
import com.me.social.service.impl.UserServiceImpl;
import com.me.social.util.UserUtil;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private UserUtil userUtil;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(userService, "maxUserPageSize", 100);
    }

}
