package merlin.example.House.Hunting.System.Apartment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.ApartmentUnit.ApartmentUnit;
import merlin.example.House.Hunting.System.Appeal.Appeal;
import merlin.example.House.Hunting.System.Campus.Campus;
import merlin.example.House.Hunting.System.Location.Location;
import merlin.example.House.Hunting.System.Report.Report;
import merlin.example.House.Hunting.System.User.User;

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
    private int views;

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
    private boolean isApproved;
    private boolean isAvailable;
    private int appealCount;
    private int reportCount;
    private double averageRating;

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

    public Apartment(User landlord,String apartmentName ,String profilePicture, String description, Location location, List<Campus> campus) {
        this.landlord = landlord;
        this.apartmentName = apartmentName;
        this.profilePicture = profilePicture;
        this.description = description;
        this.location = location;
        this.campus = campus;
        this.createdAt = LocalDateTime.now();
        this.isApproved = true;
        this.isAvailable = true;
        this.appealCount = 0;
        this.reportCount = 0;
    }
}
