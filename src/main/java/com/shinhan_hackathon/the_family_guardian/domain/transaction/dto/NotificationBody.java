package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;

public record NotificationBody(
        Long notificationId,
        TransactionType transactionType,
        String senderAccountNumber,
        String receiver,
        Long transactionBalance
) {
    public NotificationBody(Long notificationId, TransactionType transactionType, String senderAccountNumber, String receiver, Long transactionBalance) {
        this.notificationId = notificationId;
        this.transactionType = transactionType;
        this.senderAccountNumber = senderAccountNumber;
        this.receiver = receiver;
        this.transactionBalance = transactionBalance;
    }
}
