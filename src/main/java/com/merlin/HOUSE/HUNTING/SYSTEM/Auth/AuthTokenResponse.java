package com.merlin.HOUSE.HUNTING.SYSTEM.Auth;

import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;

public record AuthTokenResponse (
        String token,
        boolean mustChangePassword,
        Role role,
        String firstName
) {
}
