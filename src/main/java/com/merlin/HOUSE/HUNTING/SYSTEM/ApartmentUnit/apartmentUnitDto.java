package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import java.math.BigDecimal;

public record apartmentUnitDto(
        Long apartmentId,
        UnitType unitType,
        Integer numberOfUnits,
        Integer vacantUnits,
        BigDecimal rentPrice

) {
}
