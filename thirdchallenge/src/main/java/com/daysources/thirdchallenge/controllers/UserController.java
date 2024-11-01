package com.daysources.thirdchallenge.controllers;

import com.daysources.thirdchallenge.config.jwt.JwtToken;
import com.daysources.thirdchallenge.dto.AuthDto;
import com.daysources.thirdchallenge.dto.PasswordRequestDto;
import com.daysources.thirdchallenge.dto.UserDto;
import com.daysources.thirdchallenge.dto.UserRequestDto;
import com.daysources.thirdchallenge.services.JwtUserDetailsService;
import com.daysources.thirdchallenge.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(service.create(userRequestDto));

    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordRequestDto dto){
        service.update(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtToken> auth(@RequestBody @Validated AuthDto user, HttpServletRequest request){
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = userDetailsService.getTokenAuthenticated(user.getUsername());
            return ResponseEntity.ok(token);
        }
        catch (AuthenticationException e){
            log.warn("Bad credentials.");
        }
        return ResponseEntity.badRequest().build();
    }



}
