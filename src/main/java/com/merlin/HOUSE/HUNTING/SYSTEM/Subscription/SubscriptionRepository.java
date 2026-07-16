package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    List<Subscription> findByUserIdOrderByMadeOnDesc(Long userId);
    List<Subscription> findAllOrderByMadeOnDesc();
}
