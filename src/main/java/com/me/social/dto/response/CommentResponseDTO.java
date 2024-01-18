package com.me.social.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class CommentResponseDTO {
    private long id;
    private String content;
    private Instant creationDate;
    private boolean deleted;
}
