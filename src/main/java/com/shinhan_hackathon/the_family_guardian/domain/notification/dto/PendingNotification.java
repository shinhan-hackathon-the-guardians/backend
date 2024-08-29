package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.PaymentLimitType;

public record PendingNotification(
        Long id,
        Long userId,
        Long transactionId,
        PaymentLimitType limitType,
        String title,
        String body

) {
    public PendingNotification(Notification notification){
        this(
                notification.getId(),
                notification.getUser().getId(),
                notification.getTransaction().getId(),
                notification.getLimitType(),
                notification.getTitle(),
                notification.getBody()
        );
    }
}
