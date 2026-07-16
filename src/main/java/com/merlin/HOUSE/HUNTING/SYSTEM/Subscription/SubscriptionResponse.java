package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        String apartmentName,
        String phoneNumber,
        BigDecimal amountPaid,
        LocalDateTime madeOn,
        LocalDateTime expiresOn,
        int activeDays,
        Status status
) {}
