package com.daysources.thirdchallenge.config.jwt;

import com.daysources.thirdchallenge.services.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)){

            log.warn("Null or invalid bearer in token.");
            filterChain.doFilter(request, response);
            return;
        }
        if (!JwtUtils.isTokenValid(token)){
            log.warn("Null or invalid token.");
            filterChain.doFilter(request, response);
            return;
        }
        String username = JwtUtils.getUsernameFromToken(token);
        toAuthentication(request, username);
        filterChain.doFilter(request, response);

    }


    private void toAuthentication(HttpServletRequest request, String username){
        UserDetails userDetails = service.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, AuthorityUtils.NO_AUTHORITIES);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
