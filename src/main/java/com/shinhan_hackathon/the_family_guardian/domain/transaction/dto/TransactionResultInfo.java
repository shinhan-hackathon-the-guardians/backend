package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;

public record TransactionResultInfo(
        TransactionType transactionType,
        String senderAccountNumber,
        String receiver,
        Long transactionBalance
) {
}
