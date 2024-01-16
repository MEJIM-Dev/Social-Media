package com.me.social.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class PostResponseDTO {
    private long id;
    private String content;
    private Instant creationDate;
    private long userId;
    private long likesCount;
    private long comments;
    private boolean deleted;
}
