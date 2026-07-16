package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import java.math.BigDecimal;
import java.util.List;

public record SubscriptionDto(
        String phoneNumber,
        List<Long> apartmentId
) {
}
