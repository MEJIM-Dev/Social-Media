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

        // Set the value for maxUserPageSize in the UserServiceImpl instance
        ReflectionTestUtils.setField(userService, "maxUserPageSize", 100);
    }

    @Test
    void getUsers_Success(){
        DefaultApiResponse<?> defaultApiResponse = Mockito.mock(DefaultApiResponse.class);
        PageRequest pageRequest = PageRequest.of(0, 10);
        UserResponseDTO responseDTO = Mockito.mock(UserResponseDTO.class);
        Page<User> userList = Mockito.mock(Page.class);

        when(userUtil.userToResponseDto(Mockito.any(User.class))).thenReturn(responseDTO);
        when(userRepository.findByDeleted(false,pageRequest)).thenReturn(userList);

        DefaultApiResponse<?> response = userService.getUsers(pageRequest);

        Assertions.assertThat(response.getStatus().equals(ExtendedConstants.ResponseCode.SUCCESS.getStatus()));
    }
}
