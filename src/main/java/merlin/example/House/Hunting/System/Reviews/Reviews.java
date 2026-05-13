package merlin.example.House.Hunting.System.Reviews;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.ApartmentUnit.ApartmentUnit;
import merlin.example.House.Hunting.System.User.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "studentId"
    )
    @JsonBackReference
    private User student;

    @ManyToOne
    @JoinColumn(
            name = "apartmentUnitId"
    )
    @JsonBackReference
    private ApartmentUnit apartmentUnit;
    private double rating;
    private String comment;
    private LocalDateTime createdAt;

    public Reviews(User student, ApartmentUnit apartmentUnit, double rating, String comment) {
        this.student = student;
        this.apartmentUnit = apartmentUnit;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}
