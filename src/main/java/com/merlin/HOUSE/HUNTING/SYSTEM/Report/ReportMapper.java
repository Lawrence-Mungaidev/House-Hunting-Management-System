package com.merlin.HOUSE.HUNTING.SYSTEM.Report;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReportMapper {

    public Report toReport(ReportDto dto) {
        Report report = new Report();

        report.setComplain(dto.complain());
        report.setReportDate(LocalDateTime.now());
        report.setStatus(Status.PENDING);

        return report;
    }

    public ReportResponseDto toReportResponseDto(Report report) {
        return new ReportResponseDto(report.getId(), report.getComplain(), report.getStatus(),report.getActionTakenAt());
    }
}
