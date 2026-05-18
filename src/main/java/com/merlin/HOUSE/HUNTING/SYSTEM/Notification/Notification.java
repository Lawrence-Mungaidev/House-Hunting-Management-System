package com.merlin.HOUSE.HUNTING.SYSTEM.Notification;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(
            name = "senderId"
    )
    @JsonBackReference
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "receiverId"
    )
    @JsonBackReference
    private User receiver;
    @Column(nullable = false)
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public Notification() {}

    public Notification(User sender, User receiver, String message, NotificationType notificationType) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.notificationType = notificationType;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }
}
