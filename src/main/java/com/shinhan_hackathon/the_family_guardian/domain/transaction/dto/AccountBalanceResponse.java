package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

import lombok.Builder;

@Builder
public record AccountBalanceResponse(
	String name,
	String accountNumber,
	String transactionBalance
) {
}
