package com.daysources.thirdchallenge.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PasswordRequestDto {

    private String username;
    private String oldPassword;
    private String newPassword;
}
