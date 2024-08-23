package com.shinhan_hackathon.the_family_guardian.domain.transaction.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.DepositRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransferRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.WithdrawalRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public List<TransactionResponse> getTransactionHistory(Long userId, Pageable pageable) {
        log.info("TransactionService.getTransactionHistory() is called.");

        String accountNumber = userService.getAccountNumber(userId);
        AccountTransactionHistoryListResponse accountTransactionHistoryListResponse =
                accountService.inquireTransactionHistoryList(accountNumber);

        return accountTransactionHistoryListResponse.getRec().getList().stream()
                .map(TransactionResponse::of).toList();
    }

    // TODO: 입금
    public void updateDeposit(DepositRequest depositRequest) {
        log.info("TransactionService.updateDeposit() is called.");
        String accountNumber = userService.getAccountNumber(getUserId());
        accountService.updateAccountDeposit(accountNumber, depositRequest.transactionBalance());

        // TODO: 입금 결과에 대한 알림

        log.info("Success to make a deposit.");
    }

    /*
     * 요청을 분리해야할 필요가 있을 수 있음
     * 승인 절차 중인 요청을 어디에 보관하는가
     * - 동기식 : 어떠한 방식으로 승인 요청을 보내고 현재 작업을 중단 상태로 기다린다
     * - 비동기식 : 현재 요청을 저장하고 승인 요청을 전송, 요청에 대한 응답이 올 때 요청을 다시 진행/차단 판단
     */

    // TODO: 출금
    public void updateWithdrawal(WithdrawalRequest withdrawalRequest) {
        log.info("TransactionService.updateWithdrawal() is called.");
        String accountNumber = userService.getAccountNumber(getUserId());

        // TODO: Rule Check
        // TODO: 요청 숭인/차단을 위한 가디언에게 알림
        // TODO: 요청 승인/차단
        accountService.updateAccountDeposit(accountNumber, withdrawalRequest.transactionBalance());

        // TODO: 출금 결과에 대한 알림

        log.info("Success to withdraw account.");
    }

    // TODO: 이체
    public void updateTransfer(TransferRequest transferRequest) {
        log.info("TransactionService.updateTransfer() is called.");
        String withdrawalAccountNumber = userService.getAccountNumber(getUserId());

        // TODO: Rule Check
        // TODO: 요청 숭인/차단을 위한 가디언에게 알림
        // TODO: 요청 승인/차단
        accountService.updateAccountTransfer(transferRequest.depositAccountNumber(), withdrawalAccountNumber,
                transferRequest.transactionBalance());

        // TODO: 이체 결과에 대한 알림

        log.info("Success to transfer account.");
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }
}
