package com.merlin.HOUSE.HUNTING.SYSTEM.Media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.ApartmentUnit.ApartmentUnit;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Url;
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    private LocalDateTime createdAt;
    private String publicId;

    @ManyToOne
    @JoinColumn(
            name = "apartmentUnitId"
    )
    @JsonBackReference
    private ApartmentUnit apartmentUnit;

    public Media() {}

    public Media(String url, MediaType mediaType) {
        Url = url;
        this.mediaType = mediaType;
    }
}
