package com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit;

import com.merlin.HOUSE.HUNTING.SYSTEM.Media.Media;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ApartmentUnitMapper {

    public ApartmentUnit toApartmentUnit(apartmentUnitDto dto){
        ApartmentUnit apartmentUnit = new ApartmentUnit();

        apartmentUnit.setUnitType(dto.unitType());
        apartmentUnit.setNumberOfUnits(dto.numberOfUnits());
        apartmentUnit.setVacantUnits(dto.vacantUnits());
        apartmentUnit.setRentPrice(dto.rentPrice());
        apartmentUnit.setRentPrice(dto.rentPrice());
        apartmentUnit.setAvailable(true);
        apartmentUnit.setCreatedAt(LocalDateTime.now());
        apartmentUnit.setAverageRating(0.0);


        return apartmentUnit;
    }

    public ApartmentUnitResponse toApartmentUnitResponse(ApartmentUnit apartmentUnit){

        List<String> mediaUrlList = apartmentUnit.getMedia()
                .stream()
                .map(Media::getUrl
                ).toList();
        return new ApartmentUnitResponse(apartmentUnit.getId(),apartmentUnit.getUnitType(), apartmentUnit.getNumberOfUnits(), apartmentUnit.getVacantUnits(), apartmentUnit.getRentPrice(),apartmentUnit.getAverageRating(),mediaUrlList);
    }
}
