package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import java.math.BigDecimal;
import java.util.List;

public record ApartmentUnitResponse (
        Long apartmentId,
        UnitType unitType,
        int numberOfUnits,
        int vacantUnits,
        BigDecimal rentPrice,
        List<String> media
) {
}
