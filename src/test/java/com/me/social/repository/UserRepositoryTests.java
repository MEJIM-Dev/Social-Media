package com.me.social.repository;

import com.me.social.domain.User;
import com.me.social.domain.enumeration.Gender;
import com.me.social.dto.domain.UserFollowersDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepository_save_returnSavedUser(){
        User mockUser = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        User saveUser = userRepository.save(mockUser);

        Assertions.assertNotNull(saveUser);
        Assertions.assertTrue(saveUser.getId() > 0);
    }

    @Test
    public void findAll_Succes(){
        User mockUser1 = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();
        User mockUser2 = User.builder()
                .email("abc@mail.com")
                .deleted(false)
                .profilePicture("pic2.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Jen")
                .lastName("Bill")
                .username("bill2")
                .password("password")
                .build();

        userRepository.save(mockUser1);
        userRepository.save(mockUser2);

        List<User> all = userRepository.findAll();

        Assertions.assertTrue(!all.isEmpty());
        Assertions.assertTrue(all.size() > 1);
    }

    @Test
    public void findById_Success(){
        User mockUser1 = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        User user = userRepository.save(mockUser1);

        Optional<User> optionalUser = userRepository.findById(user.getId());

        Assertions.assertTrue(!optionalUser.isEmpty());
        Assertions.assertTrue(optionalUser.get().getId() == user.getId());
    }

    @Test
    public void findByUsernameOrEmail_Success(){
        User mockUser = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        User user = userRepository.save(mockUser);

        List<User> byUsernameOrEmail = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        Assertions.assertNotNull(byUsernameOrEmail);
        Assertions.assertNotNull(byUsernameOrEmail.get(0));
        Assertions.assertTrue(byUsernameOrEmail.get(0).getUsername().equals(mockUser.getUsername()));
        Assertions.assertTrue(byUsernameOrEmail.get(0).getEmail().equals(mockUser.getEmail()));
    }

    @Test
    public void findByIdAndDeleted_Success() {

        User mockUser = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        User user = userRepository.save(mockUser);

        List<User> result = userRepository.findByUsernameOrEmail("sam1", "ab@mail.com");


        assertEquals(1, result.size());
    }

    @Test
    public void findByUsernameAndDeleted_Success() {

        User mockUser1 = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam2")
                .password("password")
                .build();
        User mockUser2 = User.builder()
                .email("abC@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        userRepository.save(mockUser1);
        userRepository.save(mockUser2);

        Optional<User> result = userRepository.findByUsernameAndDeleted("sam1", false);

        assertEquals(false, result.isEmpty());
        assertEquals("sam1", result.get().getUsername());
    }

    @Test
    void findByDeleted_Success() {
        User mockUser1 = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam2")
                .password("password")
                .build();
        User mockUser2 = User.builder()
                .email("abc@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        userRepository.save(mockUser1);
        userRepository.save(mockUser2);

        Page<User> result = userRepository.findByDeleted(false, Pageable.unpaged());
        Page<User> result2 = userRepository.findByDeleted(true, Pageable.unpaged());

        assertEquals(2, result.getTotalElements());
        assertEquals(0, result2.getTotalElements());
    }

    @Test
    void findByEmail_Success() {
        User mockUser = User.builder()
                .email("ab@mail.com")
                .deleted(false)
                .profilePicture("pic.jpg")
                .active(true)
                .sex(Gender.MALE)
                .followersCount(0)
                .createdAt(Instant.now())
                .firstName("Sam")
                .lastName("Ben")
                .username("sam1")
                .password("password")
                .build();

        User user = userRepository.save(mockUser);

        Optional<User> result = userRepository.findByEmail("ab@mail.com");

        assertEquals(true, result.isPresent());
    }

}
