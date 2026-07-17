package com.merlin.HOUSE.HUNTING.SYSTEM.User;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User toUser(CreateAdminDto dto){
        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());
        user.setEmail(dto.email());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setTrialExpireOn(LocalDateTime.now().plusDays(30));

        return user;
    }

    public UserResponseDto toUserResponseDto(User user){
        Long campusId = user.getCampus() != null ? user.getCampus().getId() : null;

        return new UserResponseDto(user.getId(),user.getFirstName(), user.getLastName(), user.getPhoneNumber(),campusId, user.getProfilePicture());
    }
}
