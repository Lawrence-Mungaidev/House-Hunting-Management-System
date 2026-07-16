package com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.UnitType;

import java.math.BigDecimal;

public record DreamHouseResponse(
        Long apartmentUnitId,
        String apartmentName,
        BigDecimal rent,
        UnitType unitType,
        double averageRating
) {
}
