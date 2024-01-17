package com.me.social.dto.security;


import com.me.social.domain.enumeration.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JwtDecodedDTO {
    private String email;
    private Gender sex;
    private String firstName;
    private String lastName;
    private String otherNames;
    private String username;
    private String profilePicture;
    private long id;
}
