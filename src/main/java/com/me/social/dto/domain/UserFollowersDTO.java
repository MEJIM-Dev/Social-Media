package com.me.social.dto.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserFollowersDTO {
    private Long followerId;
    private Long followingId;

    public UserFollowersDTO(Long followerId,Long followingId){
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public UserFollowersDTO(long followingId){
        this.followingId = followingId;
    }
}
