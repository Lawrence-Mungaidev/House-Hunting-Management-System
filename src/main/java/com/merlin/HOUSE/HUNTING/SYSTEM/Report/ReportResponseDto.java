package com.merlin.HOUSE.HUNTING.SYSTEM.Report;

import java.time.LocalDateTime;

public record ReportResponseDto(
        Long Id,
        String complain,
        Status status,
        LocalDateTime actionTakenAt

) {
}
