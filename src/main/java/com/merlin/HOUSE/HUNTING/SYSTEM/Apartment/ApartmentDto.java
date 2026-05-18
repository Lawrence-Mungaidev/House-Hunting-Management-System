package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import java.util.List;

public record ApartmentDto(
        String apartmentName,
        String profilePicture,
        String description,
        Double apartmentLat,
        Double apartmentLong,
        List<Long> campusId
) {
}
