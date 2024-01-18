package com.me.social.config;

import com.me.social.domain.User;
import com.me.social.repository.UserRepository;
import com.me.social.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/api/v1/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        if(authorization==null || !authorization.substring(0,7).matches("Bearer ")) {
            doFilter(request, response, filterChain);
            return;
        }

        String bearerToken = authorization.substring(7);
         String username = jwtService.getSubject(bearerToken);
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(username, false);
            if(optionalUser.isEmpty()){
                throw new UsernameNotFoundException(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
            }
            User user = optionalUser.get();
            boolean validJwt = jwtService.validateJwt(bearerToken,user);

            if(validJwt) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),bearerToken, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                authenticationToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        doFilter(request, response, filterChain);
    }
}
