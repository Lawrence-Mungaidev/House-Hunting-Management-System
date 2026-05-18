package com.merlin.HOUSE.HUNTING.SYSTEM.Review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {

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
            name = "apartmentUnitId"
    )
    @JsonBackReference
    private ApartmentUnit apartmentUnit;
    private double rating;
    private String comment;
    private LocalDateTime createdAt;

    public Review(User student, ApartmentUnit apartmentUnit, double rating, String comment) {
        this.student = student;
        this.apartmentUnit = apartmentUnit;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}
