package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

public record ReviewDto(
        String comment,
        double rating
) {
}
