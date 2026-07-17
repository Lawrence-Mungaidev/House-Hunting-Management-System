package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(
            mappedBy = "subscription"
    )
    @JsonManagedReference
    private List<Apartment> apartment;
    private String phoneNumber;
    private String mpesaReference;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Subscription(){

    }

    public Subscription(List<Apartment> apartment, String phoneNumber, String mpesaReference, BigDecimal amount) {
        this.apartment = apartment;
        this.phoneNumber = phoneNumber;
        this.mpesaReference = mpesaReference;
        this.amount = amount;
    }
}
