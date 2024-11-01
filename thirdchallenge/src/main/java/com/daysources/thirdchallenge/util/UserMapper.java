package com.daysources.thirdchallenge.util;

import com.daysources.thirdchallenge.dto.UserDto;
import com.daysources.thirdchallenge.entities.User;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
public class UserMapper {

    public static User toUser(UserDto userDto){
        return new ModelMapper().map(userDto, User.class);
    }

    public static UserDto toDto(User user){
        return new ModelMapper().map(user, UserDto.class);
    }
}
