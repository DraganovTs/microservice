package com.microservices.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TwitterQueryUserDetailsService implements UserDetailsService {

   // private final QueryUserService queryUserService;

    //private final UserPermissionsToUserDetailTransformer userPermissionsToUserDetailTransformer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return TwitterQueryUser.builder()
                .username(username)
                .build();
    }
}
