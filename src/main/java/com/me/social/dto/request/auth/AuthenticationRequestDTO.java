package com.me.social.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthenticationRequestDTO {
    @NotBlank
    private String username;
    @NotNull
    @Size(min = 8,message = "Password Must Be At Least 8 Characters")
    private String password;
}
