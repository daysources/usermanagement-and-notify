package com.daysources.thirdchallenge.services;

import com.daysources.thirdchallenge.config.jwt.JwtToken;
import com.daysources.thirdchallenge.config.jwt.JwtUserDetails;
import com.daysources.thirdchallenge.config.jwt.JwtUtils;
import com.daysources.thirdchallenge.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated(String username){
        return JwtUtils.createToken(username);
    }
}
