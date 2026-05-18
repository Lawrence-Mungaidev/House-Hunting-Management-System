package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthMapper {

    public User toUser(RegisterDto dto){
        User user = new User();

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPhoneNumber(dto.phoneNumber());
        user.setRole(dto.role());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    public RegisterResponseDto toRegisterResponseDto(User user){
        return new RegisterResponseDto(user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
