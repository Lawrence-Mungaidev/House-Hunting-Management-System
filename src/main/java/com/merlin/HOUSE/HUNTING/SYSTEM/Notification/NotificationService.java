package com.merlin.HOUSE.HUNTING.SYSTEM.Notification;

import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.User;
import com.merlin.HOUSE.HUNTING.SYSTEM.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    public void createNotification(User authenticatedUser, Long receiverId,  String message,NotificationType notificationType) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFound("receiver not found"));

        Notification notification = notificationMapper.toNotification(message, notificationType);

        if(authenticatedUser != null) notification.setSender(authenticatedUser);
        else {
            notification.setSender(null);
        }

        notification.setReceiver(receiver);
        var savedUser = notificationRepository.save(notification);

        notificationMapper.toNotificationResponseDto(savedUser);
    }

    public List<NotificationResponseDto> getNotifications(User authenticatedUser) {
        Long receiverId = authenticatedUser.getId();

        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(notificationMapper :: toNotificationResponseDto)
                .toList();
    }

    public void markAsRead (Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFound("notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);

    }

    public NotificationResponseDto getNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFound("notification not found"));

       return notificationMapper.toNotificationResponseDto(notification);
    }


}
