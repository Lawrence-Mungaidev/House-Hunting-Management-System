package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Mpesa.MpesaService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.Status;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.BusinessRuleException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.Role;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Value("${subscription.amount}")
    BigDecimal subscriptionAmount;

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private  final ApartmentRepository apartmentRepository;
    private MpesaService mpesaService;

    public String createSubscription(SubscriptionDto subscriptionDto, User authenticatedUser)  {

        List<Apartment> apartmentList = new ArrayList<>();

        for( Long apartmentId : subscriptionDto.apartmentId()){
            Apartment apartment = apartmentRepository.findById(apartmentId)
                    .orElseThrow(() -> new ResourceNotFound("apartment not found"));

            if(apartment.getLandlord().getId().equals(authenticatedUser.getId())){
                apartmentList.add(apartment);
            }else {
                throw new BusinessRuleException("This apartment isn't yours ");
            }

        }
        Subscription subscription = subscriptionMapper.toSubscription(subscriptionDto);


        BigDecimal totalAmount = BigDecimal.ZERO;

        for(Apartment apartment : apartmentList){
            totalAmount = totalAmount.add(subscriptionAmount);

        }

        String checkoutRequestId = mpesaService.initialiseSTKPush(subscriptionDto.phoneNumber(), totalAmount);

        subscription.setApartment(apartmentList);
        subscription.setAmount(totalAmount);
        subscription.setMpesaReference(checkoutRequestId);
        subscription.setStatus(Status.PENDING);
        subscriptionRepository.save(subscription);

        for(Apartment apartment : apartmentList){
            apartment.setSubscription(subscription);
            apartmentRepository.save(apartment);

        }

        return "Check your phone to Complete Payment";
    }

    public  List<SubscriptionResponse>  getMySubscriptions(User authenticatedUser) {
        if(!authenticatedUser.getRole().equals(Role.LANDLORD)){
            throw new BusinessRuleException("Only LANDLORD roles are supported");
        }

        return subscriptionRepository.findByApartment_LandlordOrderByMadeOnDesc(authenticatedUser)
                .stream()
                .map(subscriptionMapper :: toSubscriptionResponse)
                .toList();
    }

    public List<SubscriptionResponse> getAllSubscriptions(User authenticatedUser) {
        if(!authenticatedUser.getRole().equals(Role.SUPER_ADMIN)){
            throw new BusinessRuleException("Only SUPER_ADMIN roles are supported");
        }

        return subscriptionRepository.findAllOrderByMadeOnDesc()
                .stream()
                .map(subscriptionMapper :: toSubscriptionResponse)
                .toList();
    }
}
