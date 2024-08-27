package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

public record NotificationReplyRequest(
        Long notificationId,
        Boolean isApprove
) {
}
