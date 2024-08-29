package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

import java.util.List;

public record PendingNotificationResponse(
        List<PendingNotification> pendingNotifications
) {
}
