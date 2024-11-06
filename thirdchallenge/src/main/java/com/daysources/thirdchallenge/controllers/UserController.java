package com.daysources.thirdchallenge.controllers;

import com.daysources.thirdchallenge.config.jwt.JwtToken;
import com.daysources.thirdchallenge.dto.AuthDto;
import com.daysources.thirdchallenge.dto.PasswordRequestDto;
import com.daysources.thirdchallenge.dto.UserDto;
import com.daysources.thirdchallenge.dto.UserRequestDto;
import static com.daysources.thirdchallenge.exceptions.ApiExceptionHandler.ErrorMessage;

import com.daysources.thirdchallenge.exceptions.InvalidCredentialsException;
import com.daysources.thirdchallenge.services.JwtUserDetailsService;
import com.daysources.thirdchallenge.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService userDetailsService;


    @Operation(summary = "User creation", description = "Endpoint for creating and persisting new users. Fetches an address through the zip code, using the ViaCEP" +
            " API.",responses =
            {
                    @ApiResponse(responseCode = "200", description = "User created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "409", description = "Username unavailable.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "502", description = "Error contacting ViaCEP API.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid zip code in request body.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(service.create(userRequestDto));

    }

    @Operation(summary = "Password update.", description = "Endpoint for updating passwords of existing users." +
            " Checks for passowrd matches and session token via JWT." +
            " API.",responses =
            {
                    @ApiResponse(responseCode = "204", description = "Password updated successfully.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Username or old password are invalid..",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Username wasn't found in the database.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordRequestDto dto){
        service.update(dto);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Authentication token", description = "Endpoint for requesting an authentication token, needed" +
            " for updating passwords in the '/api/users/update-password' endpoint.",responses =
            {
                    @ApiResponse(responseCode = "200", description = "Token generated.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials, token denied.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
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
            throw new InvalidCredentialsException("Username or password are invalid.");
        }
    }



}
