package com.shinhan_hackathon.the_family_guardian.domain.transaction.service;

import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
	private final UserService userService;
	private final TransactionRepository transactionRepository;
	private final AccountService accountService;

	public List<TransactionResponse> getTransactionHistory(Long userId, Pageable pageable) {
		log.info("TransactionService.getTransactionHistory() is called.");

		String accountNo = userService.getAccountNumber(userId);
		AccountTransactionHistoryListResponse accountTransactionHistoryListResponse =
			accountService.inquireTransactionHistoryList(accountNo);

		return accountTransactionHistoryListResponse.getRec().getList().stream()
			.map(TransactionResponse::of).toList();
	}
}
