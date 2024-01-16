package com.me.social.repository;

import com.me.social.domain.Post;
import com.me.social.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByUser(User user, PageRequest page);

    Optional<Post> findByIdAndDeleted(Long id, boolean deleted);
}
