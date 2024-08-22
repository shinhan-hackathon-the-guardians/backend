package com.shinhan_hackathon.the_family_guardian.domain.transaction.dto;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;

import lombok.Builder;

@Builder
public record TransactionResponse(
	String transactionUniqueNo,
	String transactionDate,
	String transactionTime,
	String transactionType,
	String transactionTypeName,
	String transactionAccountNo,
	String transactionBalance,
	String transactionAfterBalance,
	String transactionSummary,
	String transactionMemo
) {
	public static TransactionResponse of(AccountTransactionHistoryListResponse.Rec.Transaction transaction) {
		return TransactionResponse.builder()
			.transactionUniqueNo(transaction.getTransactionUniqueNo())
			.transactionDate(transaction.getTransactionDate())
			.transactionTime(transaction.getTransactionTime())
			.transactionType(transaction.getTransactionType())
			.transactionTypeName(transaction.getTransactionTypeName())
			.transactionAccountNo(transaction.getTransactionAccountNo())
			.transactionBalance(transaction.getTransactionBalance())
			.transactionAfterBalance(transaction.getTransactionAfterBalance())
			.transactionSummary(transaction.getTransactionSummary())
			.transactionMemo(transaction.getTransactionMemo())
			.build();
	}
}
