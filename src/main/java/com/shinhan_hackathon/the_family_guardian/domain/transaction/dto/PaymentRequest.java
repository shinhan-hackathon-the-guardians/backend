package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

public record PaymentRequest(
		String accountNumber,
        String businessName,
        Long transactionBalance
) {
}
