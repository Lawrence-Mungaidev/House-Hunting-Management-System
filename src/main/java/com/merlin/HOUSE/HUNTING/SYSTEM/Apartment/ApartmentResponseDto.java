package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

public record ApartmentResponseDto(
        Long apartmentId,
        String apartmentName,
        String profilePicture,
        String description,
        Double averageRating,
        Double distanceFromCampus,
        Double Latitude,
        Double Longitude
) {
}
