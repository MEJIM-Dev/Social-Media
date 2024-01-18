package com.me.social.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FollowRequestDTO {
    @JsonIgnore
    private Long followerId;
    @NotNull
    private Long followingId;
}
