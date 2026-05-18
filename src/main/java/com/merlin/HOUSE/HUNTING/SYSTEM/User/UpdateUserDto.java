package com.merlin.HOUSE.HUNTING.SYSTEM.User;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;

public record UpdateUserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        Long campusId,
        String profilePicture
) {
}
