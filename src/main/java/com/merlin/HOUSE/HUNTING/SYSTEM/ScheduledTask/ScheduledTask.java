package com.merlin.HOUSE.HUNTING.SYSTEM.ScheduledTask;


import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Status;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationType;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Africa/Nairobi")
    public void checkTrialExpirationAndSubscriptionExpiry(){

        List<Apartment> apartmentsList = apartmentRepository.findByStatus(Status.ACTIVE);

        for (Apartment apartment : apartmentsList) {
             User landlord = apartment.getLandlord();

             boolean trialActive = landlord.getTrialExpireOn() !=null && landlord.getTrialExpireOn().isAfter(LocalDateTime.now());

             boolean subscriptionActive = apartment.getExpireOn() != null && apartment.getExpireOn().isAfter(LocalDateTime.now());

             long daysUntilTrialEnd = landlord.getTrialExpireOn() != null ? ChronoUnit.DAYS.between(LocalDateTime.now(), landlord.getTrialExpireOn()) : -1;

             long daysUntilSubsEnd = apartment.getExpireOn() != null ? ChronoUnit.DAYS.between(LocalDateTime.now(), apartment.getExpireOn()) : -1;

            if(daysUntilTrialEnd == 7 || daysUntilSubsEnd == 7 ){
                String message ="Subscription for " + apartment.getApartmentName() + " will end in 7 days. Please Subscribe to avoid deactivation ";
                notificationService.createNotification(null, landlord.getId(), message,NotificationType.SUBSCRIBE_REMINDER);
            }

             if(daysUntilTrialEnd == 3 || daysUntilSubsEnd ==3){
                 String message ="Subscription for " + apartment.getApartmentName() + " will end in 3 days. Please Subscribe to avoid deactivation ";
                 notificationService.createNotification(null, landlord.getId(), message,NotificationType.SUBSCRIBE_REMINDER);
             }


             if (!trialActive && !subscriptionActive){
                 apartment.setStatus(Status.IN_ACTIVE);
                 String message = apartment.getApartmentName() + " has been deactivated, Please subscribe to activate it";
                 notificationService.createNotification(null, landlord.getId(), message,NotificationType.EXPIRED_SUBSCRIPTION);
                 apartmentRepository.save(apartment);
             }

        }
    }

    @Scheduled(cron = "0 0 20 * *")
    public void countViews(){

        List<Apartment> apartmentsList = apartmentRepository.findByStatus(Status.ACTIVE);

        for (Apartment apartment : apartmentsList) {
            int todayViews = apartment.getTodayViews();

            String message =apartment.getApartmentName() + " has " + todayViews + " views.";

            notificationService.createNotification(null, apartment.getLandlord().getId(), message, NotificationType.VIEWS);

            apartment.setTodayViews(0);
        }


    }

}
