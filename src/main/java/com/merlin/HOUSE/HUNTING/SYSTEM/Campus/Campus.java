package com.merlin.HOUSE.HUNTING.SYSTEM.Campus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Location.Location;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String campusName;
    @OneToOne
    @JoinColumn(
            name = "locationId"
    )
    private Location location;
    private LocalDateTime createdAt;
    private boolean isActive;
    @OneToMany(
            mappedBy = "campus"
    )
    @JsonManagedReference
    private List<User> user;

    @ManyToMany(
            mappedBy = "campus"
    )
    @JsonIgnore
    private List<Apartment> apartments;

    @OneToMany(
            mappedBy = "adminCampus"
    )
    @JsonManagedReference
    private List<User> admins;

    public Campus() {}

    public Campus(String campusName, Location location) {
        this.campusName = campusName;
        this.location = location;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
}
