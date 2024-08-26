package com.shinhan_hackathon.the_family_guardian.domain.transaction.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;
import java.util.List;

import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import com.shinhan_hackathon.the_family_guardian.global.fcm.FcmSender;
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
    private final PaymentLimitService paymentLimitService;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final FcmSender fcmSender;
    private final AuthUtil authUtil;

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
        User user = userService.getUser(userId);
        accountService.updateAccountDeposit(user.getAccountNumber(), depositRequest.transactionBalance());

        // TODO: 입금 결과에 대한 알림
        DepositInfo depositInfo = new DepositInfo(user.getName(), user.getAccountNumber(), depositRequest.transactionBalance());
        String deviceToken = authUtil.getUserPrincipal().getDeviceToken();
        fcmSender.sendMessage(deviceToken, "입금", depositInfo.toString());

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
                 * Notification 로직
                 * -----------------
                 *
                 * TODO: 승인 요청 결과
                 * if() {} // 승인 요청 허가 -> 출금 -> 출금 완료 알림
                 * TODO: 허가 시 출금을 어떻게 구현? -> Notification 쪽에서 해야할듯
                 * else {} // 승인 요청 불허 -> 출금 거부 알림
                 *
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

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }
}
