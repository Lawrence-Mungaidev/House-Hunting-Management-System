package com.merlin.HOUSE.HUNTING.SYSTEM.Subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(
            name = "landlordId"
    )
    @JsonBackReference
    private User landlord;
    private String phoneNumber;
    private String mpesaReference;
    private BigDecimal amount;
    private LocalDateTime madeOn;
    private LocalDateTime expiredOn;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Subscription(){

    }

    public Subscription(User landlord, String phoneNumber, BigDecimal amount) {
        this.landlord = landlord;
        this.phoneNumber = phoneNumber;
        this.madeOn = LocalDateTime.now();
        this.status = Status.FREE_TRIAL;
        this.amount = amount;
    }
}
