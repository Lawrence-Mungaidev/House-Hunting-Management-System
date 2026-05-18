package com.merlin.HOUSE.HUNTING.SYSTEM.Report;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "studentId"
    )
    @JsonBackReference
    private User student;
    @ManyToOne
    @JoinColumn(
            name = "apartmentId"
    )
    @JsonBackReference
    private Apartment apartment;
    private String complain;
    private LocalDateTime reportDate;
    private LocalDateTime viewDate;
    private Status status;

    public Report(User student, Apartment apartment, String complain) {
        this.student = student;
        this.apartment = apartment;
        this.complain = complain;
        this.status = Status.PENDING;
        this.reportDate = LocalDateTime.now();
    }
}
