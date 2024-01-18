package com.me.social.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentUpdateDTO {
    @NotBlank
    private String content;
    @JsonIgnore
    private Long userId;
}
