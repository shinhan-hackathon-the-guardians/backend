package com.shinhan_hackathon.the_family_guardian.domain.notification.repository;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "select * from notification where user_id = :userId", nativeQuery = true)
    List<Notification> findByUserId(Long userId);
}
