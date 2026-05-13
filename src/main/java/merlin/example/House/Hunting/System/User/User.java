package merlin.example.House.Hunting.System.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.Apartment.Apartment;
import merlin.example.House.Hunting.System.Appeal.Appeal;
import merlin.example.House.Hunting.System.Campus.Campus;
import merlin.example.House.Hunting.System.CampusRequest.CampusRequest;
import merlin.example.House.Hunting.System.DreamHouse.DreamHouse;
import merlin.example.House.Hunting.System.Notification.Notification;
import merlin.example.House.Hunting.System.Report.Report;
import merlin.example.House.Hunting.System.Reviews.Reviews;
import merlin.example.House.Hunting.System.Subscription.Subscription;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private String profilePicture;
    @ManyToOne
    @JoinColumn(
            name = "campusId"
    )
    @JsonBackReference
    private Campus campus;

    @OneToMany(
            mappedBy = "requestedBy"
    )
    @JsonManagedReference
    private List<CampusRequest> campusRequest;

    @OneToMany(
            mappedBy = "landlord"
    )
    @JsonManagedReference
    private List<Apartment> apartment;

    @OneToMany(
            mappedBy = "landlord"
    )
    @JsonManagedReference
    private List<Subscription> subscriptions;

    @OneToMany(
            mappedBy = "sender"
    )
    @JsonManagedReference
    private List<Notification> notificationsMade;

    @OneToMany(
            mappedBy = "receiver"
    )
    @JsonManagedReference
    private List<Notification> receivedNotifications;

    @OneToMany(
            mappedBy = "student"
    )
    @JsonManagedReference
    private List<Reviews> reviews;

    @OneToMany(
            mappedBy = "student"
    )
    @JsonManagedReference
    private List<DreamHouse> dreamHouses;

    @OneToMany(
            mappedBy = "student"
    )
    @JsonManagedReference
    private List<Report> reports;

    @OneToMany(
            mappedBy = "landlord"
    )
    @JsonManagedReference
    private List<Appeal> appeals;

    public User(String firstName, String lastName, String email, String phoneNumber, String password, Campus campus, Role role, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
        this.createdAt = LocalDateTime.now();
        this.campus = campus;
        this.isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of( new SimpleGrantedAuthority("ROLE_" + role.name()) );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
