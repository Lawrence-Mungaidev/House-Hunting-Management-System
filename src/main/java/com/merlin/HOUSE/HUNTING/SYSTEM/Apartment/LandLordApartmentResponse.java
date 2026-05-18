package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusDto;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusResponseDto;

import java.util.List;

public record LandLordApartmentResponse(
        Long apartmentId,
        String apartmentName,
        String profilePicture,
        String description,
        List<CampusResponseDto> campusList
) {
}
