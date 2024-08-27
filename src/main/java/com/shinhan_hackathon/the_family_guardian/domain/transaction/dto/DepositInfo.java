package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

public record DepositInfo(
        String name,
        String accountNumber,
        Long transactionBalance
) {
}
