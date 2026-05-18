package com.merlin.HOUSE.HUNTING.SYSTEM.Campus;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CampusMapper {

    public Campus toCampus(CampusDto dto){
        Campus campus = new Campus();
        campus.setCampusName(dto.campusName());
        campus.setCreatedAt(LocalDateTime.now());
        campus.setActive(true);
        return campus;
    }

    public CampusResponseDto toCampusResponseDto(Campus campus){
        return new CampusResponseDto(campus.getId(), campus.getCampusName(), campus.getLocation());
    }

}
