package com.me.social.dto.request;

import com.me.social.domain.enumeration.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RegistrationDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @Size(min=8, max=100)
    private String password;
    @NotBlank
    private String profilePicture;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{3,}")
    private String firstName;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{3,}")
    private String lastName;
    private String otherNames;
    @NotNull
    private Gender sex;
}
