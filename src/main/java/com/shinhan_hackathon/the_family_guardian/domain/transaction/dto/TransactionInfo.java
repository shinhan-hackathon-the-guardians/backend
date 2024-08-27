package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.PaymentLimitType;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;

public record TransactionInfo (
        User user,
        Transaction transaction,
        PaymentLimitType limitType
) {
}
