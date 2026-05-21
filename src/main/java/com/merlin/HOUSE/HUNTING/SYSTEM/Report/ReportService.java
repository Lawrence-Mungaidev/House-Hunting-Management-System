package com.merlin.HOUSE.HUNTING.SYSTEM.Report;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationType;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final NotificationService notificationService;
    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;

    public ReportResponseDto createReport(User authenticatedUser, ReportDto dto, Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Apartment Not Found"));

        if(reportRepository.existsByStudentAndApartmentAndStatus(authenticatedUser, apartment, Status.PENDING)){
            throw new BusinessRuleException("You already have a pending report for this apartment");
        }

        Report report = reportMapper.toReport(dto);

        report.setStudent(authenticatedUser);
        report.setApartment(apartment);

        var savedReport = reportRepository.save(report);

        Long landLordId = apartment.getLandlord().getId();

        apartment.setReportCount(apartment.getReportCount() + 1);

        int reportCount = reportRepository.countReportsByApartment(apartment);

        if(reportCount== 3){

            String landLordMessage = "You have been Reported " + reportCount + " times if it reaches 5 your apartment will be deactivated";
            notificationService.createNotification(authenticatedUser,landLordId,landLordMessage, NotificationType.REPORT);
        }

         if(reportCount >= 5){
             String deactivatedMessage = "Your apartment " + apartment.getApartmentName() + " has been temporarily been deactivated";
             apartment.setActive(false);
             notificationService.createNotification(authenticatedUser,landLordId,deactivatedMessage, NotificationType.REPORT );
        }

        apartmentRepository.save(apartment);

        List<User> admins = userRepository.findByRole(Role.ADMIN);

        String adminMessage = apartment.getApartmentName() +" has been Reported";

        for (User admin : admins) {
            Long userId = admin.getId();

            notificationService.createNotification(authenticatedUser,userId,adminMessage, NotificationType.REPORT);
        }

        return reportMapper.toReportResponseDto(savedReport);
    }

    public List<ReportResponseDto> getAllRReports(){
        return reportRepository.findAll()
                .stream()
                .map(reportMapper :: toReportResponseDto)
                .toList();
    }

    public List<ReportResponseDto> getReportByApartment(Long apartmentId){
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ResourceNotFound("Apartment Not Found"));

        return reportRepository.findReportsByApartment(apartment)
                .stream()
                .map(reportMapper :: toReportResponseDto)
                .toList();
    }

    public List<ReportResponseDto> getReportByStatus(Status status){
        return reportRepository.findReportsByStatus(status)
                .stream()
                .map(reportMapper :: toReportResponseDto)
                .toList();
    }

    public void reviewReport (User authenticatedUser,Long reportId){
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFound("Report Not Found"));

        if(!report.getStatus().equals(Status.PENDING)){
            throw new BusinessRuleException("This report has already been  resolved");
        }
        report.setStatus(Status.RESOLVED);
        report.setActionTakenAt(LocalDateTime.now());
        reportRepository.save(report);

        Long receiverId = report.getStudent().getId();

        String studentMessage = "Your Report " + report.getComplain() +" has been Resolved";

        notificationService.createNotification(authenticatedUser, receiverId,studentMessage,NotificationType.REPORT_RESOLVED);
    }

    public void dismissReport(User authenticatedUser,Long  reportId){
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFound("Report Not Found"));

        if(!report.getStatus().equals(Status.PENDING)){
           throw new BusinessRuleException("This report has already been Dismissed");
        }

        report.setStatus(Status.DISMISSED);
        report.setActionTakenAt(LocalDateTime.now());
        reportRepository.save(report);

        Long receiverId = report.getStudent().getId();

        String studentMessage = "Your Report " + report.getComplain() +" has been Dismissed, This means the admin didn't see anything wrong";

        notificationService.createNotification(authenticatedUser, receiverId,studentMessage,NotificationType.REPORT_DISMISSED);
    }

    public List<ReportResponseDto> getMyReports(User authenticatedUser){
        return reportRepository.findReportsByStudent(authenticatedUser)
                .stream()
                .map(reportMapper :: toReportResponseDto)
                .toList();
    }



}
