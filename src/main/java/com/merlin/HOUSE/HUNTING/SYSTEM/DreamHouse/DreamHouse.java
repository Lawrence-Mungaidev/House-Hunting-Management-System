package com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse;

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
public class DreamHouse {

    @Id
    @GeneratedValue
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
    private ApartmentUnit apartmentUnit;
    private LocalDateTime createdAt;

    public DreamHouse(User student, ApartmentUnit apartmentUnit) {
        this.student = student;
        this.apartmentUnit = apartmentUnit;
        this.createdAt = LocalDateTime.now();
    }
}
