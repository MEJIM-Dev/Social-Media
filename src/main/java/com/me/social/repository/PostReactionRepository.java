package com.me.social.repository;

import com.me.social.domain.Post;
import com.me.social.domain.PostReaction;
import com.me.social.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction,Long> {

    Optional<PostReaction> findByPostAndUser(Post post, User user);
}
