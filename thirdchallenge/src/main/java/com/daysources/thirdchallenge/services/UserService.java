package com.daysources.thirdchallenge.services;

import com.daysources.thirdchallenge.dto.PasswordRequestDto;
import com.daysources.thirdchallenge.dto.UserDto;
import com.daysources.thirdchallenge.dto.UserRequestDto;
import com.daysources.thirdchallenge.entities.Address;
import com.daysources.thirdchallenge.entities.User;
import com.daysources.thirdchallenge.repositories.UserRepository;
import com.daysources.thirdchallenge.util.AddressMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RabbitSender rabbit;
    private final PasswordEncoder passwordEncoder;

    public Address findByPostal(String cep){
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        ResponseEntity<Address> response = restTemplate.getForEntity(url, Address.class);

        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        else{
            throw new RuntimeException("API Error - Service unavailable or incorrect postal code.");
        }
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(("User not found."))
        );
    }

    public UserDto create (UserRequestDto userRequestDto){
        userRequestDto.setPostalCode(userRequestDto.getPostalCode().replace("-",""));
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

    public void update (PasswordRequestDto request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            throw new RuntimeException("User not found.");
        }
        else {
            User updatedUser = user.get();
            if(!passwordEncoder.matches(request.getOldPassword(), updatedUser.getPassword())){
                System.out.println(request.toString());
                throw new RuntimeException("Invalid old password.");

            }
            else {
                updatedUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(updatedUser);
                rabbit.sendUserActionMessage("Username: "+ request.getUsername() + " | Operation: UPDATE");
            }

        }
    }

}
