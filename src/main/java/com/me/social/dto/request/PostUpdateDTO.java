package com.me.social.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostUpdateDTO {

    @NotBlank
    private String content;
    @JsonIgnore
    private long userId;
}
