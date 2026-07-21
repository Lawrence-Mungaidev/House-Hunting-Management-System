package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusDto;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusMapper;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.CampusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApartmentMapper {
    
    private final CampusMapper campusMapper;

    public Apartment toApartment(ApartmentDto dto){
        Apartment apartment = new Apartment();
        apartment.setProfilePicture(dto.profilePicture());
        apartment.setApartmentName(dto.apartmentName());
        apartment.setApartmentName(dto.apartmentName());
        apartment.setCreatedAt(LocalDateTime.now());
        apartment.setAppealsCount(0);
        apartment.setReportCount(0);
        apartment.setAverageRating(0.0);

        return apartment;
    }

    public ApartmentResponseDto  toApartmentResponseDto(Apartment apartment, Double distanceFromCampus){

        Double Latitude = apartment.getLocation().getLatitude();
        Double Longitude = apartment.getLocation().getLongitude();

        return  new ApartmentResponseDto(apartment.getId(), apartment.getApartmentName(), apartment.getProfilePicture(), apartment.getDescription(), apartment.getAverageRating(), Latitude,Longitude, distanceFromCampus);
    }

    public LandLordApartmentResponse toLandLordApartmentResponse(Apartment apartment){

        return new LandLordApartmentResponse(apartment.getId(), apartment.getApartmentName(), apartment.getProfilePicture(), apartment.getDescription(), toResponseList(apartment.getCampus()));
    }

    private List<CampusResponseDto>  toResponseList(List<Campus> campusList){

        List<CampusResponseDto> campusResponseDtoList = new ArrayList<>();

        for(Campus campus : campusList){
            campusResponseDtoList.add(campusMapper.toCampusResponseDto(campus));
        }

        return campusResponseDtoList;
    }


}
