package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;

public record RegisterDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Long  campusId,
        String password,
        Role role
) {
}
