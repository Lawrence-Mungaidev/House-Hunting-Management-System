package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SubscriptionResponse(
        String apartmentName,
        String phoneNumber,
        BigDecimal amountPaid,
        LocalDateTime madeOn,
        LocalDateTime expiresOn,
        Long activeDays,
        Status status
) {}
