package com.merlin.HOUSE.HUNTING.SYSTEM.Notification;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public Notification toNotification(String message,NotificationType notificationType) {
        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notification;
    }

    public NotificationResponseDto toNotificationResponseDto(Notification notification) {

        Long senderId = notification.getSender().getId() != null ? notification.getSender().getId() : null;

        return  new NotificationResponseDto(notification.getId(),senderId,notification.getMessage(),notification.getNotificationType());
    }
}
