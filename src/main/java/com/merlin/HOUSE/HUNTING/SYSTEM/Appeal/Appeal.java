package com.merlin.HOUSE.HUNTING.SYSTEM.Appeal;

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
public class Appeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "apartmentId"
    )
    @JsonBackReference
    private Apartment apartment;
    @ManyToOne
    @JoinColumn(
            name = "landlordId"
    )
    @JsonBackReference
    private User landlord;
    private String message;
    private Status status;
    private LocalDateTime createdAt;
    private String adminResponse;
    private LocalDateTime responseDate;

    public Appeal(){}

    public Appeal(Apartment apartment, User landlord, String message) {
        this.apartment = apartment;
        this.landlord = landlord;
        this.message = message;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();

    }
}
