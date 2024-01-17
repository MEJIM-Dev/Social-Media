package com.me.social.service.impl;

import com.me.social.domain.User;
import com.me.social.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Override
    public String getSubject(String token) {
        return getClaim(token,Claims::getSubject);
    }

    @Override
    public String generateJwt(User userDetails){
        return generateJwt(userDetails,new HashMap<>());
    }

    @Override
    public String generateJwt(
            User user,
            Map<String, Object> extraClaims
    ){
        return Jwts.
                builder()
                .addClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15 ))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateJwt(String token, User userDetails){
        String userName = getSubject(token);
        return userName.equals(userDetails.getUsername()) && tokenIsNonExpired(token);
    }

    @Override
    public boolean tokenIsNonExpired(String token) {
        return extractExpiration(token).after(new Date(System.currentTimeMillis()));
    }

    @Override
    public Date extractExpiration(String token) {
        return getClaim(token,Claims::getExpiration);
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        Claims allClaims = getAllClaims(token);
        return claimsResolver.apply(allClaims);
    }

    @Override
    public Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
}
