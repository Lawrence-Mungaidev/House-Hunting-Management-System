package merlin.example.House.Hunting.System.Campus;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.Apartment.Apartment;
import merlin.example.House.Hunting.System.Location.Location;
import merlin.example.House.Hunting.System.User.User;

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

    public Campus(String campusName, Location location) {
        this.campusName = campusName;
        this.location = location;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
}
