package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

import java.time.LocalDateTime;

public record LandlordAppealResponseDto(
        Long Id,
        String message,
        LocalDateTime createdDate,
        Status status,
        String adminResponse,
        LocalDateTime adminResponseDate
) {
}
