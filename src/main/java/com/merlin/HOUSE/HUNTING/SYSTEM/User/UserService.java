package com.merlin.HOUSE.HUNTING.SYSTEM.User;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.DuplicateResourceException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CampusRepository campusRepository;

    public UserResponseDto createAdmin(CreateAdminDto dto){
        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new DuplicateResourceException("Email already exists");
        }

        Campus campus = campusRepository.findById(dto.campusId())
                .orElseThrow(() -> new ResourceNotFound("Campus not found"));

        User user = userMapper.toUser(dto);
        user.setCampus(campus);
        user.setRole(Role.ADMIN);
        //password Harshing

        var savedUser = userRepository.save(user);

        return userMapper.toUserResponseDto(savedUser);
    }

    public UserResponseDto updateUser(User authenticatedUser ,UpdateUserDto dto){
        Long id = authenticatedUser.getId();

        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("User not found"));

        if(dto.firstName() != null){
            user.setFirstName(dto.firstName());
        }
        if(dto.lastName() != null){
            user.setLastName(dto.lastName());
        }
        if(dto.phoneNumber() != null){
            user.setPhoneNumber(dto.phoneNumber());
        }
        if(dto.campusId() != null){
            Campus campus = campusRepository.findById(dto.campusId())
                    .orElseThrow(() -> new ResourceNotFound("Campus not found"));

            user.setCampus(campus);
        }
        // make a method for deleting and updating a profile pic
        if(dto.profilePicture() != null){
            user.setProfilePicture(dto.profilePicture());
        }
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper:: toUserResponseDto)
                .toList();
    }

    public List<UserResponseDto> getAllAdmins(){
        return userRepository.findByRole(Role.ADMIN)
                .stream()
                .map(userMapper :: toUserResponseDto)
                .toList();
    }

    public List<UserResponseDto> getAllUsersByRole(Role role){
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper :: toUserResponseDto)
                .toList();
    }

   public void activateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFound("User not found"));

        user.setActive(true);
        userRepository.save(user);
   }

   public void deactivateUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFound("User not found"));

        user.setActive(false);
        userRepository.save(user);
   }

}
