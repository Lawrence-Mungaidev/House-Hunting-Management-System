package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class SubscriptionMapper {

    public Subscription toSubscription(SubscriptionDto dto){
        Subscription subscription = new Subscription();
        subscription.setPhoneNumber(dto.phoneNumber());
        subscription.setMadeOn(LocalDateTime.now());

        return subscription;
    }

    public SubscriptionResponse toSubscriptionResponse(Subscription subscription){
                Long activeDays = ChronoUnit.DAYS.between(LocalDateTime.now(), subscription.getExpiredOn());

      return   new SubscriptionResponse(
                subscription.getApartment().getApartmentName(),
                subscription.getPhoneNumber(),subscription.getAmount(),
                subscription.getMadeOn(),
                subscription.getExpiredOn(),
                activeDays,
                Status.ACTIVE);

    }
}
