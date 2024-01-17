package com.me.social.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CreatePostDTO {
    @NotBlank
    @Size(max = 255)
    private String content;
    @JsonIgnore
    private long userId;
}
