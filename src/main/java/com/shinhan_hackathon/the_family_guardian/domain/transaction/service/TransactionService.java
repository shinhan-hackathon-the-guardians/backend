package com.shinhan_hackathon.the_family_guardian.domain.transaction.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.ResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.DepositRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.PaymentRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionResponse;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransferRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.WithdrawalRequest;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {
    private final UserService userService;
    private final PaymentLimitService paymentLimitService;
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
        Long userId = getUserId();
        String accountNumber = userService.getAccountNumber(userId);
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
        Long userId = getUserId();
        String accountNumber = userService.getAccountNumber(userId);

        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, withdrawalRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, withdrawalRequest.transactionBalance())) { // 승인
                accountService.updateAccountDeposit(accountNumber, withdrawalRequest.transactionBalance()); // 출금
                // TODO: 출금 성공 알림
            }
            else  { // 단건 한도 초과
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김

                /*
                 * Notification 로직 or EventListener?
                 * -----------------
                 *
                 * TODO: 승인 요청 결과 수신 :
                 * TODO: transactionService.updateTransactionApproveCount() // Approve & Reject Count 증가
                 * TODO: if(checkAlert) {
                 * TODO:     Switch(transactionType) {Withdrawal, Transfer, Payment} // Transaction Type 확인:
                 * TODO:     transactionService.executeWithdrawalTransaction() & // Transaction 실행 :
                 * TODO:                        executeTransferTransaction() &
                 * TODO:                        executePaymentTransaction()
                 * TODO:     // 승인 알림 전송 & Transaction 종료 판정?
                 * TODO: }
                 * TODO: else if() {
                 * TODO:     // 차단 알림 전송
                 * TODO: }
                 * TODO: else {
                 * TODO:     // 아무 일도 없음
                 * TODO: }
                 *
                 * TODO: 시간제한 확인 -> 어떻게 구현? -> @Scheduler?
                 */
            }
        }
        else { // 총 한도 초과
            // TODO: 출금 거부 알림
        }

        log.info("Success to withdraw account.");
    }

    // TODO: 이체
    public void updateTransfer(TransferRequest transferRequest) {
        log.info("TransactionService.updateTransfer() is called.");
        Long userId = getUserId();
        String withdrawalAccountNumber = userService.getAccountNumber(userId);

        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, transferRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, transferRequest.transactionBalance())) { // 승인
                accountService.updateAccountTransfer(transferRequest.depositAccountNumber(), withdrawalAccountNumber, transferRequest.transactionBalance()); // 이체
                // TODO: 이체 성공 알림
            }
            else  { // 단건 한도 초과
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김

            }
        }
        else { // 총 한도 초과
            // TODO: 이체 거부 알림
        }

        log.info("Success to transfer account.");
    }

    // TODO: 결제 : 승인이 필요한 요청, 아닌 요청을 어떻게 구분?
    public void updatePayment(PaymentRequest paymentRequest) {
        log.info("TransactionService.updatePayment() is called.");
        Long userId = getUserId();
        String accountNumber = userService.getAccountNumber(userId);

        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, paymentRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, paymentRequest.transactionBalance())) { // 승인
                accountService.updateAccountDeposit(accountNumber, paymentRequest.transactionBalance()); // 결제
                // TODO: 결제 성공 알림
            }
            else  { // 단건 한도 초과
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김

            }
        }
        else { // 총 한도 초과
            // TODO: 결제 거부 알림
        }

        log.info("Success to payment account.");
    }

    // TODO: REFUSE에 의해 승인 요구치에 절대 도달하지 못하는 경우
    // TODO: 시간제한에 걸려 자동으로 차단되는 경우


    // TODO: 승인 요구치에 따른 출금 진행
    public void executeWithdrawalTransaction(Long transactionId) {
        log.info("TransactionService.executeWithdrawalTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 출금
            accountService.updateAccountWithdrawal(user.getAccountNumber(), transaction.getTransactionBalance());
        }
        // TODO: 이 로직을 분리할지에 대한 판단 필요
        // TODO: else if((totalMangerCount - transaction.getRejectCount()) < approvalRequirement) {
        // TODO: 거부 알림
    }

    // TODO: 승인 요구치에 따른 이체 진행 - DepositAccountNo를 받아야함, 수정 있을 수 있음
    public void executeTransferTransaction(Long transactionId, String depositAccountNo) {
        log.info("TransactionService.executeTransferTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 이체
            accountService.updateAccountTransfer(depositAccountNo, user.getAccountNumber(), transaction.getTransactionBalance());
        }
    }

    // TODO: 승인 요구치에 따른 결제 진행
    public void executePaymentTransaction(Long transactionId) {
        log.info("TransactionService.executePaymentTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 이체
            accountService.updateAccountWithdrawal(user.getAccountNumber(), transaction.getTransactionBalance());
        }
    }

    // TODO: 현재 Transaction Count 증가
    @Transactional
    public Transaction updateTransactionApproveCount(Long transactionId, ResponseStatus responseStatus) {
        log.info("TransactionService.updateTransactionApproveCount() is called.");
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));

        if(responseStatus.equals(ResponseStatus.APPROVE)) {
            transaction.incrementApproveCount();
        }
        else if(responseStatus.equals(ResponseStatus.DENY)) {
            transaction.incrementRejectCount(); // TODO: 거절 Count 로직 추가 필요
        }
        else {
            throw new RuntimeException("Failed to found response status.");
        }

        transactionRepository.save(transaction);
        return transaction;
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }


}
