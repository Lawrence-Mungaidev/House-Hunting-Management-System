package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByApartment_LandlordOrderByMadeOnDesc(User landlord);
    List<Subscription> findAllOrderByMadeOnDesc();
    Optional<Subscription> findByMpesaReference(String checkoutRequestId);
}
