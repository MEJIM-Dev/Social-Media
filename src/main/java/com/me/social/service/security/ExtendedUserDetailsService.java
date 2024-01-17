package com.me.social.service.security;

import com.me.social.config.ExtendedConstants;
import com.me.social.domain.User;
import com.me.social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExtendedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(username, false);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(ExtendedConstants.ResponseCode.INVALID_USER.getMessage());
        }
        User dbUser = optionalUser.get();
        UserDetails user = new org.springframework.security.core.userdetails.User(dbUser.getUsername(), dbUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return user;
    }
}
