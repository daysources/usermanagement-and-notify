package com.daysources.thirdchallenge.config.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private com.daysources.thirdchallenge.entities.User user;

    public JwtUserDetails (com.daysources.thirdchallenge.entities.User user){
        super(user.getUsername(), user.getPassword(), AuthorityUtils.NO_AUTHORITIES);
        this.user = user;
    }

    public Long getId(){
        return this.user.getId();
    }
}
