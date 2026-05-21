package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationType;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppealService {

    private final AppealRepository appealRepository;
    private final AppealMapper appealMapper;
    private final ApartmentRepository apartmentRepository;
    private final NotificationService notificationService;

    public LandlordAppealResponseDto landlordAppeal(User authenicatedUser, LandLordAppealDto dto, Long apartmentId) {

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("apartment not found"));

        if(appealRepository.existsByLandLordANDApartmentAndStatus(authenicatedUser,apartment,Status.PENDING)){
            throw new BusinessRuleException("You already have a pending appeal, Please wait for the Admins response");
        }

        Appeal appeal = appealMapper.toAppeal(dto);

        appeal.setApartment(apartment);
        appeal.setLandlord(authenicatedUser);

        var savedAppeal = appealRepository.save(appeal);

        return appealMapper.toLandLordAppealResponseDto(savedAppeal);
    }

    public AdminResponseDto approveAppeal(AdminAppealDto dto, Long appealId, Status status,User authenticatedUser) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new ResourceNotFound("appeal not found"));

        if(!appeal.getStatus().equals(Status.PENDING)){
            throw new BusinessRuleException("This appeal has already been handled");
        }

        Apartment apartment = appeal.getApartment();


        appeal.setAdminResponse(dto.adminResponse());
        appeal.setStatus(Status.APPROVED);
        apartment.setActive(true);
        appeal.setAdmin(authenticatedUser);
        appeal.setResponseDate(LocalDateTime.now());
        apartmentRepository.save(apartment);

        var savedAppeal = appealRepository.save(appeal);
        notificationService.createNotification(authenticatedUser,appeal.getLandlord().getId(),dto.adminResponse(), NotificationType.APPEAL_Notification);

        return appealMapper.toAdminResponseDto(savedAppeal);

    }

    public AdminResponseDto rejectAppeal(AdminAppealDto dto, Long appealId, User authenticatedUser) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new ResourceNotFound("appeal not found"));

        if(!appeal.getStatus().equals(Status.PENDING)){
            throw new BusinessRuleException("This appeal has already been handled");
        }

        appeal.setAdminResponse(dto.adminResponse());
        appeal.setStatus(Status.REJECTED);
        appeal.setAdmin(authenticatedUser);
        appeal.setResponseDate(LocalDateTime.now());

        var savedAppeal = appealRepository.save(appeal);
        notificationService.createNotification(authenticatedUser,appeal.getLandlord().getId(),dto.adminResponse(), NotificationType.APPEAL_Notification);

        return appealMapper.toAdminResponseDto(savedAppeal);

    }

    public List<LandlordAppealResponseDto> landLordAppeals(User authenticatedUser){
        return appealRepository.findByLandLordOrderByCreatedAtDesc(authenticatedUser)
                .stream()
                .map(appealMapper :: toLandLordAppealResponseDto)
                .toList();
    }

    public List<AdminResponseDto> adminsAppeals(User authenticatedUser){
        return appealRepository.findByAdminOrderBYResponseDate(authenticatedUser)
                .stream()
                .map(appealMapper :: toAdminResponseDto)
                .toList();
    }

    public LandlordAppealResponseDto getLandlordAppealById(Long appealId) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(()-> new ResourceNotFound("appeal not found"));

        return appealMapper.toLandLordAppealResponseDto(appeal);
    }

    public AdminResponseDto getAdminAppealById(Long appealId) {
        Appeal appeal = appealRepository.findById(appealId)
                .orElseThrow(()-> new ResourceNotFound("appeal not found"));

        return appealMapper.toAdminResponseDto(appeal);
    }



}
