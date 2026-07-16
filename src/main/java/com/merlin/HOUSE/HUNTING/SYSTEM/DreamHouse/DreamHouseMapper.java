package com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse;

import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DreamHouseMapper {

    public DreamHouse toDreamHouse(ApartmentUnit apartmentUnit){
        DreamHouse dreamHouse = new DreamHouse();
        dreamHouse.setApartmentUnit(apartmentUnit);
        dreamHouse.setCreatedAt(LocalDateTime.now());
        return dreamHouse;
    }


    public DreamHouseResponse toDreamHouseResponse(ApartmentUnit apartmentUnit, Long dreamHouseId) {
        return new DreamHouseResponse(dreamHouseId, apartmentUnit.getApartment().getApartmentName(),apartmentUnit.getRentPrice(),apartmentUnit.getUnitType(),apartmentUnit.getAverageRating());
    }

}
