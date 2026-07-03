package com.waste.portal.service;

import com.waste.portal.model.Notification;
import com.waste.portal.model.User;
import com.waste.portal.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findByUser(User user) {
        return notificationRepository.findByRecipientOrRecipientIsNullOrderByCreatedDateDesc(user);
    }

    public long getUnreadCount(User user) {
        if (user == null) return 0;
        return notificationRepository.countByRecipientAndIsReadFalse(user);
    }

    @Transactional
    public Notification createNotification(String title, String content, User recipient) {
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .recipient(recipient)
                .createdDate(LocalDateTime.now())
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(User user) {
        if (user == null) return;
        List<Notification> notifications = notificationRepository.findByRecipientOrRecipientIsNullOrderByCreatedDateDesc(user);
        for (Notification n : notifications) {
            if (n.getRecipient() != null && n.getRecipient().getId().equals(user.getId()) && !n.isRead()) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        }
    }
}
