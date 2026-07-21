package com.merlin.HOUSE.HUNTING.SYSTEM.Apartment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.Subscription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;
import com.merlin.HOUSE.HUNTING.SYSTEM.Appeal.Appeal;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;
import com.merlin.HOUSE.HUNTING.SYSTEM.Report.Report;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String apartmentName;
    private String profilePicture;
    @ManyToOne
    @JoinColumn(
            name = "landlordId"
    )
    @JsonBackReference
    private User landlord;
    private String description;
    private int todayViews;
    private int totalViews;
    private boolean flaggedApartment = false;

    @OneToOne
    @JoinColumn(
            name = "locationId"
    )
    private Location location;

    @ManyToMany
    @JoinTable(
        name = "apartment_courses",
        joinColumns = {
                @JoinColumn(name = "apartmentId")
        },
            inverseJoinColumns = {
                @JoinColumn(name = "campusId")
            }
    )
    private List<Campus> campus;
    private LocalDateTime createdAt;
    private int reportCount;
    private double averageRating;
    private int appealsCount;
    private Status status;
    private LocalDateTime madeOn;
    private LocalDateTime expireOn;
    private boolean distanceFlagged = false;

    @OneToMany(
            mappedBy = "apartment"
    )
    @JsonManagedReference
    private List<ApartmentUnit> apartmentUnits;

    @OneToMany(
            mappedBy = "apartment"
    )
    @JsonManagedReference
    private List<Appeal> appeals;

    @OneToMany(
            mappedBy = "apartment"
    )
    @JsonManagedReference
    private List<Report> reports;

   @ManyToOne
   @JoinColumn(
           name = "subscriptionId"
   )
    private Subscription subscription;

    public Apartment(){

    }

    public Apartment(User landlord,String apartmentName ,String profilePicture, String description, Location location, List<Campus> campus) {
        this.landlord = landlord;
        this.apartmentName = apartmentName;
        this.profilePicture = profilePicture;
        this.description = description;
        this.location = location;
        this.campus = campus;
        this.createdAt = LocalDateTime.now();
        this.reportCount = 0;
    }
}
