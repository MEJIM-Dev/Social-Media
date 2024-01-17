package com.me.social.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthenticationToken {
    private String access_token;
    private String refresh_token;
}
