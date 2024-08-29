package com.shinhan_hackathon.the_family_guardian.domain.notification.repository;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.NotificationResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationResponseStatusRepository extends JpaRepository<NotificationResponseStatus, Long> {
    Optional<NotificationResponseStatus> findByGuardianAndNotification(User guardian, Notification notification);
}
