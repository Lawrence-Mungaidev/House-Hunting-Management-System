package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Config.JWTService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.DuplicateResourceException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
import lombok.RequiredArgsConstructor;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final CampusRepository campusRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

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

    public AuthTokenResponse logIn(logInDto dto){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.email(),
                            dto.password()
                    )
            );
        }catch(BadCredentialsException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or passwprd");
        }

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        String jwt = jwtService.generateToken(user);

        return new AuthTokenResponse(jwt,user.isMustChangePassword(),user.getRole(),user.getFirstName());

    }
}
