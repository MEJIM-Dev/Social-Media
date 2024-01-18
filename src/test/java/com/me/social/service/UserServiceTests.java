package com.me.social.service;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.domain.enumeration.Gender;
import com.me.social.dto.response.DefaultApiResponse;
import com.me.social.dto.response.UserResponseDTO;
import com.me.social.repository.UserRepository;
import com.me.social.service.impl.UserServiceImpl;
import com.me.social.util.UserUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserUtil userUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Value("${app.users.maxPageSize}")
    private int maxUserPageSize;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(userService, "maxUserPageSize", 100);
    }

    @Test
    public void getUsers_Success(){
//         Arrange
        Pageable pageable = PageRequest.of(0,10);
        Page<User> mock = new PageImpl<>(List.of(new User(),new User()));
        UserResponseDTO responseDTO= new UserResponseDTO();

        when(userRepository.findByDeleted(false, pageable)).thenReturn(mock);
        when(userUtil.userToResponseDto(Mockito.any())).thenReturn(responseDTO);

        // Act
        DefaultApiResponse<?> apiResponse = userService.getUsers(pageable);

        // Assert
        assertEquals(ExtendedConstants.ResponseCode.SUCCESS.getStatus(), apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
//        Page<User> users = mock(Page.class);
//
//        when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(users);
//
//        DefaultApiResponse<?> response = userService.getUsers(PageRequest.of(1, 10));
//
//        Assertions.assertThat(response).isNotNull();

    }

    private void assertEquals(String status, String status1) {

    }
}
