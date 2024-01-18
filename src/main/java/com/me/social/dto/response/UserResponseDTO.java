package com.me.social.dto.response;

import com.me.social.domain.enumeration.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class UserResponseDTO {
    private String email;
    private String username;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String otherNames;
    private Gender sex;
    private Instant createdAt;
    private Long id;
    private Long followersCount;
}
