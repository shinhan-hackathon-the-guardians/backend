package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;

public record NotificationHistory(
        Long notificationId,
        TransactionType transactionType,
        Long transactionBalance
) {
}
