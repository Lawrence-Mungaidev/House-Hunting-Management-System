package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.Config.IpUtils;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/Apartments")
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final IpUtils ipUtils;

    @PostMapping("/create")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<LandLordApartmentResponse> createApartment(@Valid @RequestBody ApartmentDto apartmentDto,
                                                                     @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apartmentService.createApartment(authenticatedUser, apartmentDto));
    }

    @PatchMapping("/update/{apartmentId}")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<LandLordApartmentResponse> updateApartment(@Valid @RequestBody ApartmentDto apartmentDto,
                                                                     @PathVariable("apartmentId") Long apartmentId,
                                                                     @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.updateApartment(apartmentId,apartmentDto,authenticatedUser));
    }

    @PatchMapping("/activate/{apartmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> activateApartment(@PathVariable Long apartmentId){
        apartmentService.activateApartment(apartmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate/{apartmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateApartment (@PathVariable Long apartmentId){
        apartmentService.deactivateApartment(apartmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{apartmentId}")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<Void>  deleteApartment(@PathVariable Long apartmentId, @AuthenticationPrincipal User authenticatedUser){
        apartmentService.deleteApartment(apartmentId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity <List<ApartmentResponseDto>> getAllApartments(){
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.getAllApartments());
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentResponseDto> getApartment(
            @PathVariable Long apartmentId,
            @AuthenticationPrincipal User authenticatedUser, HttpServletRequest request){

        String ip = ipUtils.getClientIp(request);
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.getApartmentById(authenticatedUser,apartmentId,ip));
    }

    @GetMapping("/myapartment")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<List<LandLordApartmentResponse>> getMyApartments(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.getMyApartment(authenticatedUser));
    }

    @GetMapping("/campus/{campusId}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentsByCampus(@PathVariable Long campusId, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.getApartmentsByCampus(campusId,authenticatedUser));
    }


    // search apartments by using max rent,minimum rent and unity type
    @GetMapping("/categorise/{campusId}")
    public ResponseEntity<List<ApartmentResponseDto>> getApartmentByCategory(@PathVariable Long campusId, @RequestBody SearchDto dto ,@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status((HttpStatus.OK)).body(apartmentService.searchApartment(authenticatedUser,dto,campusId));
    }

    @GetMapping("/searchByName/{campusId}")
    public ResponseEntity<List<ApartmentResponseDto>> searchApartmentByName(@RequestBody String apartmentName, @PathVariable Long campusId, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.OK).body(apartmentService.searchApartmentByName(apartmentName,authenticatedUser,campusId));
    }


}
