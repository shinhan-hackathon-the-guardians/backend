package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

public record DepositRequest(
		String accountNumber,
        Long transactionBalance
) {
}
