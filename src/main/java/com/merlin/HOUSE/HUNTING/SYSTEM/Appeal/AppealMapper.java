package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppealMapper {

    public Appeal toAppeal(AppealDto dto){
        Appeal appeal = new Appeal();
        appeal.setMessage(dto.message());
        appeal.setStatus(Status.PENDING);
        appeal.setCreatedAt(LocalDateTime.now());

        return appeal;
    }

    public LandlordAppealResponseDto toLandLordAppealResponseDto(Appeal appeal){
        return new LandlordAppealResponseDto(appeal.getId(), appeal.getMessage(), appeal.getCreatedAt(),appeal.getStatus(),appeal.getAdminResponse(),appeal.getResponseDate());
    }

    public AdminResponseDto toAdminResponseDto(Appeal appeal){

        String landLordName = appeal.getLandlord().getFirstName() + " " + appeal.getLandlord().getLastName();
        String apartmentName = appeal.getApartment().getApartmentName();

        return new AdminResponseDto(appeal.getId(), appeal.getMessage(), appeal.getStatus(), appeal.getAdminResponse(),landLordName,apartmentName,appeal.getCreatedAt(),appeal.getResponseDate());
    }
}
