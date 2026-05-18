package com.merlin.HOUSE.HUNTING.SYSTEM.Campus;

import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;

public record CampusResponseDto(
        Long campusId,
        String campusName,
        Location location
) {
}
