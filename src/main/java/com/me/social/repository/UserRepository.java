package com.me.social.repository;

import com.me.social.domain.User;
import com.me.social.dto.domain.UserFollowersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByIdAndDeleted(Long id, boolean deleted);

    Page<User> findByDeleted(boolean deleted, Pageable pageable);

    Optional<User> findByUsernameAndDeleted(String username, boolean deleted);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM user_followers a WHERE a.following_id = :followingId AND a.follower_id = :followerId", nativeQuery = true)
    Long followersCount(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    default boolean isAlreadyFollowing(Long followerId, Long followingId){
        return followersCount(followerId,followingId)>0;
    };

    @Query(value = "INSERT INTO user_followers (following_id, follower_id) VALUES(:followingId, :followerId) RETURNING follower_id, following_id",nativeQuery = true)
    List<Object[]> saveFollowing(@Param("followerId") Long followerId,@Param("followingId") Long followingId);

    default UserFollowersDTO followUser(Long followerId, Long followingId){
        List<Object[]> result = saveFollowing(followerId, followingId);
        if(result.isEmpty()){
            return null;
        }
        Object[] entry = result.get(0);
        Long follower_id = (Long) entry[0];
        Long following_id = (Long) entry[1];
        return new UserFollowersDTO(follower_id, following_id);
    }

    @Query(value = "DELETE FROM user_followers WHERE follower_id = :followerId AND following_id = :followingId", nativeQuery = true)
    void unfollowUser(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

}
