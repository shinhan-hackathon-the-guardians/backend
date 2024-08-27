package com.shinhan_hackathon.the_family_guardian.domain.notification.repository;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
