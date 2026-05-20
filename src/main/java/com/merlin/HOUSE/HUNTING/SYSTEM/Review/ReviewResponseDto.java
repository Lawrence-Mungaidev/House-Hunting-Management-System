package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

public record ReviewResponseDto(
        Long reviewId,
        String comment,
        double rating
) {
}
