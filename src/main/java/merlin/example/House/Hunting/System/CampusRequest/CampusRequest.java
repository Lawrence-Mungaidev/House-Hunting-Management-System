package merlin.example.House.Hunting.System.CampusRequest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.User.User;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class CampusRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "requestedBy_Id"
    )
    @JsonBackReference
    private User requestedBy;
    private String campusName;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String rejectReason;
    private LocalDateTime rejectedDate;
    private LocalDateTime approvedDate;
    private LocalDateTime createdAt;

    public CampusRequest(User requestedBy, String campusName) {
        this.requestedBy = requestedBy;
        this.campusName = campusName;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }
}
