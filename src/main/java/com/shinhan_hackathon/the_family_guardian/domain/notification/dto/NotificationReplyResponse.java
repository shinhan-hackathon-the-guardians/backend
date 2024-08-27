package com.shinhan_hackathon.the_family_guardian.domain.notification.dto;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionStatus;

public record NotificationReplyResponse(
        Long transactionId,
        TransactionStatus transactionStatus,
        Integer approveCount,
        Integer rejectCount
) {
}
