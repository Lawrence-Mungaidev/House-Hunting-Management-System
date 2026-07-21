package com.merlin.HOUSE.HUNTING.SYSTEM.User;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;

public record CreateAdminDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        Long campusId,
        String profilePicture
) {
}
