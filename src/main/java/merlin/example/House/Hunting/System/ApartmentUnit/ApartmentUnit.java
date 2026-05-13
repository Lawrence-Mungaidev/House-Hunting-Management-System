package merlin.example.House.Hunting.System.ApartmentUnit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.Apartment.Apartment;
import merlin.example.House.Hunting.System.DreamHouse.DreamHouse;
import merlin.example.House.Hunting.System.Media.Media;
import merlin.example.House.Hunting.System.Report.Report;
import merlin.example.House.Hunting.System.Reviews.Reviews;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ApartmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "apartmentId"
    )
    @JsonBackReference
    private Apartment apartment;
    @Enumerated(EnumType.STRING)
    private UnitType unitType;
    private int numberOfUnits;
    private int vacantUnits;
    @OneToMany(
            mappedBy = "apartmentUnit"
    )
    @JsonManagedReference
    private List<Media> media;
    private BigDecimal rentPrice;
    private LocalDateTime createdAt;
    private boolean isAvailable;
    private double averageRating;

    @OneToMany(
            mappedBy = "apartmentUnit"
    )
    @JsonManagedReference
    private List<Reviews> reviews;

    @OneToMany(
            mappedBy = "apartmentUnit"
    )
    @JsonManagedReference
    private List<DreamHouse> dreamHouses;


    public ApartmentUnit(Apartment apartment, UnitType unitType, int numberOfUnits, int vacantUnits, BigDecimal rentPrice) {
        this.apartment = apartment;
        this.unitType = unitType;
        this.numberOfUnits = numberOfUnits;
        this.vacantUnits = vacantUnits;
        this.rentPrice = rentPrice;
        this.createdAt = LocalDateTime.now();
        this.isAvailable = true;
    }
}
