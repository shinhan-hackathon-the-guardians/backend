package com.shinhan_hackathon.the_family_guardian.domain.notification.entity;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "NotificationResponse")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class NotificationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guardian_id", nullable = false)
    private User guardian;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(nullable = false)
    private boolean approved;

    @Builder
    public NotificationResponse(User guardian, Notification notification, boolean approved) {
        this.guardian = guardian;
        this.notification = notification;
        this.approved = approved;
    }
}
