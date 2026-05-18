package com.merlin.HOUSE.HUNTING.SYSTEM.Location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;

@Entity
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;

    @OneToOne(
            mappedBy = "location"
    )
    @JsonBackReference
    private Campus campus;

    @OneToOne(
            mappedBy = "location"
    )
    @JsonBackReference
    private Apartment apartment;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
