package com.merlin.HOUSE.HUNTING.SYSTEM.Notification;


public record NotificationDTO(
        Long receiverId,
        String message,
        NotificationType notificationType
){

}
