package com.me.social.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class PostResponseDTO {
    private Long id;
    private String content;
    private Instant creationDate;
    private Long userId;
    private Long likesCount;
    private Integer comments;
    private Boolean deleted;
}
