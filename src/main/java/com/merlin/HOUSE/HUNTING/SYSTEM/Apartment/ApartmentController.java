package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/Apartment")
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping("/create")
    public ResponseEntity<LandLordApartmentResponse> createApartment(@Valid @RequestBody ApartmentDto apartmentDto,
                                                                     @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apartmentService.createApartment(authenticatedUser, apartmentDto));
    }


}
