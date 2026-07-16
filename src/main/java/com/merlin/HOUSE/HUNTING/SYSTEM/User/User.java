package com.merlin.HOUSE.HUNTING.SYSTEM.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Appeal.Appeal;
import com.merlin.HOUSE.HUNTING.SYSTEM.Campus.Campus;
import com.merlin.HOUSE.HUNTING.SYSTEM.CampusRequest.CampusRequest;
import com.merlin.HOUSE.HUNTING.SYSTEM.DreamHouse.DreamHouse;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.Notification;
import com.merlin.HOUSE.HUNTING.SYSTEM.Report.Report;
import com.merlin.HOUSE.HUNTING.SYSTEM.Review.Review;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.Subscription;
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
    private List<Review> reviews;

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

    @ManyToOne
    @JoinColumn(
            name = "adminCampusId"
    )
    private Campus adminCampus;

    @OneToMany(
            mappedBy = "admin"
    )
    @JsonManagedReference
    private List<Appeal> adminsAppeals;

    public User(){

    }

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
