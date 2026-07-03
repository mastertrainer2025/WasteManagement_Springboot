package com.waste.portal.repository;

import com.waste.portal.model.Notification;
import com.waste.portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrRecipientIsNullOrderByCreatedDateDesc(User recipient);
    long countByRecipientAndIsReadFalse(User recipient);
}
