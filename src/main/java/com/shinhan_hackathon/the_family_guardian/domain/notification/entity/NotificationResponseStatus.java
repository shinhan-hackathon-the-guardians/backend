package com.shinhan_hackathon.the_family_guardian.domain.notification.entity;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "NotificationResponseStatus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class NotificationResponseStatus {

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
    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;

    @Builder
    public NotificationResponseStatus(User guardian, Notification notification, ResponseStatus responseStatus) {
        this.guardian = guardian;
        this.notification = notification;
        this.responseStatus = responseStatus;
    }
}
