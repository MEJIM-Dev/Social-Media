package com.me.social.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentRequest {
    @NotNull
    private Long postId;
    @NotBlank
    @Size(min = 1,max = 255)
    private String comment;
    @JsonIgnore
    private Long userId;
}
