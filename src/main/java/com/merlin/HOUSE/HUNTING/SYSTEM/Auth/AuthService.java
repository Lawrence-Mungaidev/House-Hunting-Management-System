package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.DuplicateResourceException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
import lombok.RequiredArgsConstructor;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final CampusRepository campusRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDto register(RegisterDto dto){

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new DuplicateResourceException("Email already exists");
        }

        User user = authMapper.toUser(dto);

        Campus campus = campusRepository.findById(dto.campusId())
                .orElseThrow(() -> new ResourceNotFound("Campus not found"));

        user.setCampus(campus);

        if(dto.role().equals(Role.LANDLORD)){
            user.setTrialExpireOn(LocalDateTime.now().plusDays(30));

        }

        String hashedPassword = passwordEncoder.encode(dto.password());

        user.setPassword(hashedPassword);

        var savedUser = userRepository.save(user);

        return authMapper.toRegisterResponseDto(savedUser);
    }
}
