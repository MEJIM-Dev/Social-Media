package com.me.social.repository;

import com.me.social.domain.Comment;
import com.me.social.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByPost(Post post, PageRequest page);

    Page<Comment> findByDeleted(boolean deleted, Pageable pageable);

    Page<Comment> findByPostAndDeleted(Post post, boolean deleted, Pageable pageable);

    Optional<Comment> findByIdAndDeleted(Long id, boolean deleted);
}
