package com.daysources.thirdchallenge.services;

import com.daysources.thirdchallenge.dto.AddressDto;
import com.daysources.thirdchallenge.dto.PasswordRequestDto;
import com.daysources.thirdchallenge.dto.UserDto;
import com.daysources.thirdchallenge.dto.UserRequestDto;
import com.daysources.thirdchallenge.entities.Address;
import com.daysources.thirdchallenge.entities.User;
import com.daysources.thirdchallenge.exceptions.ApiCallErrorException;
import com.daysources.thirdchallenge.exceptions.InvalidCredentialsException;
import com.daysources.thirdchallenge.exceptions.InvalidZipcodeException;
import com.daysources.thirdchallenge.exceptions.UnavailableUsernameException;
import com.daysources.thirdchallenge.repositories.UserRepository;
import com.daysources.thirdchallenge.util.AddressCall;
import com.daysources.thirdchallenge.util.AddressMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service @RequiredArgsConstructor
public class UserService {

    private final AddressCall addressCall;
    private final UserRepository userRepository;
    private final RabbitSender rabbit;
    private final PasswordEncoder passwordEncoder;

    public Address findByPostal(String cep){
        ResponseEntity<Address> response = addressCall.getByPostal(cep);
        if (response.getStatusCode()!= HttpStatus.OK){
            throw new ApiCallErrorException("Error contacting the address fetch service. Please try again later.");
        }
        AddressDto dto = AddressMapper.toDto(response.getBody());
        if (dto.getStreet() == null || dto.getCity() == null){
            log.warn("Incorrect CEP format or sequence.");
            throw new InvalidZipcodeException("The zip code is invalid, please check and try again.");
        }
        return response.getBody();
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(("User not found."))
        );
    }

    public UserDto create (UserRequestDto userRequestDto){
        try {
            userRequestDto.setPostalCode(userRequestDto.getPostalCode().replace("-", ""));
            Address address = findByPostal(userRequestDto.getPostalCode());
            User user = new User();
            user.setUsername(userRequestDto.getUsername());
            user.setEmail(userRequestDto.getEmail());
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
            user.setCep(userRequestDto.getPostalCode());
            user.setAddress(address);
            userRepository.save(user);

        rabbit.sendUserActionMessage("Username: "+ userRequestDto.getUsername() + " | Operation: CREATE");

        return new UserDto(user.getUsername(), user.getEmail(), AddressMapper.toDto(address));
        }
        catch (DataIntegrityViolationException e){
            throw new UnavailableUsernameException("The username provided is already in use. Please try again with a different one.");
        }

    }

    public void update (PasswordRequestDto request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            throw new InvalidCredentialsException("User not found.");
        }
        else {
            User updatedUser = user.get();
            if(!passwordEncoder.matches(request.getOldPassword(), updatedUser.getPassword())){
                throw new InvalidCredentialsException("Old password does not match database.");

            }
            else {
                updatedUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(updatedUser);
                rabbit.sendUserActionMessage("Username: "+ request.getUsername() + " | Operation: UPDATE");
            }

        }
    }

}
