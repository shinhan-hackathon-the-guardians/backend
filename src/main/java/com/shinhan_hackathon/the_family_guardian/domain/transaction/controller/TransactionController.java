package com.shinhan_hackathon.the_family_guardian.domain.transaction.controller;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.DepositRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransferRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.WithdrawalRequest;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionService;

	@GetMapping("/history")
	public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@RequestParam Long userId,
																		   @PageableDefault(size = 20)
																		   Pageable pageable) {
		log.info("TransactionController.getTransactionHistory() is called.");
		return ResponseEntity.ok(transactionService.getTransactionHistory(userId, pageable));
	}


	// TODO: 입금
	@PostMapping("/deposit")
	public ResponseEntity<Void> updateDeposit(@RequestBody DepositRequest depositRequest) {
		log.info("TransactionController.updateDeposit() is called.");
		log.info("{}", depositRequest);
		transactionService.updateDeposit(depositRequest);

		return ResponseEntity.ok().build();
	}

	// TODO: 출금
	@PostMapping("/withdrawal")
	public ResponseEntity<Void> updateWithdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
		log.info("TransactionController.updateWithdrawal() is called.");
		transactionService.updateWithdrawal(withdrawalRequest);

		return ResponseEntity.ok().build();
	}

	// TODO: 이체
	@PostMapping("/transfer")
	public ResponseEntity<Void> updateTransfer(@RequestBody TransferRequest transactionBalance) {
		log.info("TransactionController.updateTransfer() is called.");
		transactionService.updateTransfer(transactionBalance);

		return ResponseEntity.ok().build();
	}
}
