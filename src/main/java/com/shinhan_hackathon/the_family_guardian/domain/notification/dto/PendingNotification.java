package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

public record PendingNotification(
        Long userId,
        String name,
        Integer notificationCount
) {
}
