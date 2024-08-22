package com.shinhan_hackathon.the_family_guardian.domain.transaction.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
