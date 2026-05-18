package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.DuplicateResourceException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final CampusRepository campusRepository;

    public RegisterResponseDto register(RegisterDto dto){

        if(userRepository.findByEmail(dto.email()).isPresent()){
            throw new DuplicateResourceException("Email already exists");
        }

        User user = authMapper.toUser(dto);

        Campus campus = campusRepository.findById(dto.campusId())
                .orElseThrow(() -> new ResourceNotFound("Campus not found"));

        user.setCampus(campus);

        // password Harshing

        var savedUser = userRepository.save(user);

        return authMapper.toRegisterResponseDto(savedUser);
    }
}
