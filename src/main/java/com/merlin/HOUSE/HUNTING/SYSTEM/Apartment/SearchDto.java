package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.UnitType;

import java.math.BigDecimal;

public record SearchDto(
        BigDecimal minRent,
        BigDecimal maxRent,
        UnitType unitType
) {
}
