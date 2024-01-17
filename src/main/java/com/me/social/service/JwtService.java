package com.me.social.service;

import com.me.social.domain.User;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String getSubject(String token);

    String generateJwt(User userDetails);

    String generateJwt(
            User user,
            Map<String, Object> extraClaims
    );

    boolean validateJwt(String token, User userDetails);

    boolean tokenIsNonExpired(String token);

    Date extractExpiration(String token);

    <T> T getClaim (String token, Function<Claims, T> claimsResolver);

    Claims getAllClaims(String token);

    Key getSignInKey();
}
