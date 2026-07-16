package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(
            name = "apartmentId"
    )
    private Apartment apartment;
    private String phoneNumber;
    private String mpesaReference;
    private BigDecimal amount;
    private LocalDateTime madeOn;
    private LocalDateTime expireOn;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Subscription(){

    }

    public Subscription(Apartment apartment, String phoneNumber, BigDecimal amount) {
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.madeOn = LocalDateTime.now();
        this.status = Status.FREE_TRIAL;
        this.amount = amount;
    }
}
