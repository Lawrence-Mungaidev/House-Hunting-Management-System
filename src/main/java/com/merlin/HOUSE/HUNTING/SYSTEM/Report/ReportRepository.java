package com.merlin.HOUSE.HUNTING.SYSTEM.Report;

import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    int countReportsByApartment(Apartment apartment);
    List<Report> findReportsByApartment(Apartment apartment);
    List<Report> findReportsByStatus(Status status);
    boolean existsByStudentAndApartmentAndStatus (User student, Apartment apartment, Status status);
    List<Report> findReportsByStudent(User student);
}
