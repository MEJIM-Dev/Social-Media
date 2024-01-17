package com.me.social.repository;

import com.me.social.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsernameOrEmail(String username, String email);

    List<User> findByUsernameOrEmailAndDeleted(String username, String email, boolean deleted);

    Optional<User> findByIdAndDeleted(Long id, boolean deleted);

    Page<User> findByDeleted(boolean deleted, Pageable pageable);

    Optional<User> findByUsernameAndDeleted(String username, boolean deleted);

    Optional<User> findByEmail(String email);
}
