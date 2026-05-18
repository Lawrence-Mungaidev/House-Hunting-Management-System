package com.merlin.HOUSE.HUNTING.SYSTEM.Notification;

public record NotificationResponseDto(
        Long notificationId,
        Long senderId,
        String message,
        NotificationType notificationType
) {
}
