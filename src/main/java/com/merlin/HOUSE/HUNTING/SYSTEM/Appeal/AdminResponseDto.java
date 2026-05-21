package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

import java.time.LocalDateTime;

public record AdminResponseDto(
        Long Id,
        String message,
        Status status,
        String adminResponse,
        String landlordName,
        String apartmentName,
        LocalDateTime createdAt,
        LocalDateTime responseDate

) {
}
