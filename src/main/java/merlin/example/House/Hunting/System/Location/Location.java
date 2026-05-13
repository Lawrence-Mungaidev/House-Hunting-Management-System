package merlin.example.House.Hunting.System.Location;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.Apartment.Apartment;
import merlin.example.House.Hunting.System.Campus.Campus;

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
