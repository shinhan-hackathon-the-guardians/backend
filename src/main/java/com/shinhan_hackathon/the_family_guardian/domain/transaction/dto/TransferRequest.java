package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

public record TransferRequest(
		String withdrawalAccountNumber,
        String depositAccountNumber,
        Long transactionBalance
) {
}
