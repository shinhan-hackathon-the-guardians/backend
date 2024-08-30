package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;

public record UnansweredNotification(
        Long notificationId,
        String senderName,
        String transactionTime,
        Long transactionBalance,
        TransactionType transactionType,
        String accountNumber
) {
}
