package com.shinhan_hackathon.the_family_guardian.domain.transaction.service;

import com.shinhan_hackathon.the_family_guardian.bank.dto.response.AccountTransactionHistoryListResponse;
import com.shinhan_hackathon.the_family_guardian.bank.service.AccountService;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.PaymentLimitType;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.ResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.notification.service.NotificationService;
import com.shinhan_hackathon.the_family_guardian.domain.payment.service.PaymentLimitService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.*;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionStatus;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.service.UserService;
import com.shinhan_hackathon.the_family_guardian.global.auth.dto.UserPrincipal;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.shinhan_hackathon.the_family_guardian.global.event.PaymentApproveEvent;
import com.shinhan_hackathon.the_family_guardian.global.event.TransferApproveEvent;
import com.shinhan_hackathon.the_family_guardian.global.event.WithdrawalApproveEvent;
import com.shinhan_hackathon.the_family_guardian.global.fcm.FcmSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final FcmSender fcmSender;
    private final NotificationService notificationService;
    private final List<Role> familyGuardianRole = List.of(Role.MANAGER, Role.OWNER);
    private final int TIMEOUT = 10;

    public List<TransactionResponse> getTransactionHistory(Long userId, Pageable pageable) {
        log.info("TransactionService.getTransactionHistory() is called.");

        String accountNumber = userService.getAccountNumber(userId);
        AccountTransactionHistoryListResponse accountTransactionHistoryListResponse =
                accountService.inquireTransactionHistoryList(accountNumber);

        return accountTransactionHistoryListResponse.getRec().getList().stream()
                .map(TransactionResponse::of).toList();
    }

    // TODO: 입금
    @Transactional
    public void updateDeposit(DepositRequest depositRequest) {
        log.info("TransactionService.updateDeposit() is called.");
        Long userId = getUserId();
        User user = userService.getUser(userId);
        accountService.updateAccountDeposit(user.getAccountNumber(), depositRequest.transactionBalance());

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .transactionBalance(depositRequest.transactionBalance())
                .status(TransactionStatus.APPROVE)
                .build();
        transactionRepository.save(transaction);
        // TODO: 입금 결과에 대한 알림
        DepositInfo depositInfo = new DepositInfo(user.getName(), user.getAccountNumber(), depositRequest.transactionBalance());
        fcmSender.sendMessage(user.getDeviceToken(), "입금", depositInfo.toString());

        log.info("Success to make a deposit.");
    }

    /*
     * 요청을 분리해야할 필요가 있을 수 있음
     * 승인 절차 중인 요청을 어디에 보관하는가
     * - 동기식 : 어떠한 방식으로 승인 요청을 보내고 현재 작업을 중단 상태로 기다린다
     * - 비동기식 : 현재 요청을 저장하고 승인 요청을 전송, 요청에 대한 응답이 올 때 요청을 다시 진행/차단 판단
     */

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
     * --------------
     * TODO: 시간제한 확인 -> 어떻게 구현? -> @Scheduler?
     */

    // TODO: .1 출금
    @Transactional
    public void updateWithdrawal(WithdrawalRequest withdrawalRequest) {
        log.info("TransactionService.updateWithdrawal() is called.");
        Long userId = getUserId();
        User user = userService.getUser(userId);

        TransactionStatus transactionStatus = null;
        PaymentLimitType paymentLimitType = null;
        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, withdrawalRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, withdrawalRequest.transactionBalance())) { // 승인
                accountService.updateAccountDeposit(user.getAccountNumber(), withdrawalRequest.transactionBalance()); // 출금
                // TODO: 출금 성공 알림

                transactionStatus = TransactionStatus.APPROVE;
            }
            else  { // 단건 한도 초과
                transactionStatus = TransactionStatus.PENDING;
                paymentLimitType = PaymentLimitType.SINGLE_TRANSACTION_LIMIT;
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김


            }
        }
        else { // 총 한도 초과
            // TODO: 출금 거부 알림
            transactionStatus = TransactionStatus.REJECT;
            paymentLimitType = PaymentLimitType.MAX_LIMIT_AMOUNT;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAWAL)
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .transactionBalance(withdrawalRequest.transactionBalance())
                .status(transactionStatus)
                .approveCount(0)
                .build();
        transactionRepository.save(transaction);


        switch (transactionStatus) {
            case APPROVE -> fcmSender.sendWithdrawalSuccessMessage(user.getDeviceToken(), user.getAccountNumber(), withdrawalRequest.transactionBalance());
            case REJECT -> fcmSender.sendWithdrawalFailMessage(user.getDeviceToken(), user.getAccountNumber(), withdrawalRequest.transactionBalance());
            case PENDING -> sendGuardianApprovalMessage(user, transaction, paymentLimitType);
        }

    }

    // TODO: 2. 이체
    @Transactional
    public void updateTransfer(TransferRequest transferRequest) {
        log.info("TransactionService.updateTransfer() is called.");
        Long userId = getUserId();
        User user = userService.getUser(userId);
        String withdrawalAccountNumber = user.getAccountNumber();

        TransactionStatus transactionStatus = null;
        PaymentLimitType paymentLimitType = null;
        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, transferRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, transferRequest.transactionBalance())) { // 승인
                accountService.updateAccountTransfer(transferRequest.depositAccountNumber(), withdrawalAccountNumber, transferRequest.transactionBalance()); // 이체

                // TODO: 이체 성공 알림
                transactionStatus = TransactionStatus.APPROVE;
            }
            else  { // 단건 한도 초과
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김
                transactionStatus = TransactionStatus.PENDING;
                paymentLimitType = PaymentLimitType.SINGLE_TRANSACTION_LIMIT;

            }
        }
        else { // 총 한도 초과
            // TODO: 이체 거부 알림
            transactionStatus = TransactionStatus.REJECT;
            paymentLimitType = PaymentLimitType.MAX_LIMIT_AMOUNT;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.TRANSFER)
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .transactionBalance(transferRequest.transactionBalance())
                .status(transactionStatus)
                .approveCount(0)
                .receiver(transferRequest.depositAccountNumber())
                .build();
        transactionRepository.save(transaction);

        switch (transactionStatus) {
            case APPROVE -> fcmSender.sendTransferSuccessMessage(user.getDeviceToken(), withdrawalAccountNumber, transferRequest.depositAccountNumber(), transferRequest.transactionBalance());
            case REJECT -> fcmSender.sendTransferFailMessage(user.getDeviceToken(), withdrawalAccountNumber, transferRequest.depositAccountNumber(), transferRequest.transactionBalance());
            case PENDING -> sendGuardianApprovalMessage(user, transaction, paymentLimitType);
        }
    }


    // TODO: 3. 결제 : 승인이 필요한 요청, 아닌 요청을 어떻게 구분?
    @Transactional
    public void updatePayment(PaymentRequest paymentRequest) {
        log.info("TransactionService.updatePayment() is called.");
        Long userId = getUserId();
        User user = userService.getUser(userId);

        TransactionStatus transactionStatus = null;
        PaymentLimitType paymentLimitType = null;
        // TODO: Rule Check
        if(paymentLimitService.checkMaxAmountLimit(userId, paymentRequest.transactionBalance())) {
            if(paymentLimitService.checkSingleTransactionLimit(userId, paymentRequest.transactionBalance())) { // 승인
                accountService.updateAccountDeposit(user.getAccountNumber(), paymentRequest.transactionBalance()); // 결제
                // TODO: 결제 성공 알림
                transactionStatus = TransactionStatus.APPROVE;
            }
            else  { // 단건 한도 초과
                // TODO: 승인 요청 알림 -- 여기서 끊고, Notification에 역할을 넘김
                transactionStatus = TransactionStatus.PENDING;
                paymentLimitType = PaymentLimitType.SINGLE_TRANSACTION_LIMIT;

            }
        }
        else { // 총 한도 초과
            // TODO: 결제 거부 알림
            transactionStatus = TransactionStatus.REJECT;
            paymentLimitType = PaymentLimitType.MAX_LIMIT_AMOUNT;
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PAYMENT)
                .timestamp(Timestamp.from(Instant.now()))
                .user(user)
                .transactionBalance(paymentRequest.transactionBalance())
                .status(transactionStatus)
                .approveCount(0)
                .receiver(paymentRequest.businessName())
                .build();
        transactionRepository.save(transaction);


        switch (transactionStatus) {
            case APPROVE -> fcmSender.sendPaymentSuccessMessage(user.getDeviceToken(), user.getAccountNumber(), paymentRequest.businessName(), paymentRequest.transactionBalance());
            case REJECT -> fcmSender.sendPaymentFailMessage(user.getDeviceToken(), user.getAccountNumber(), paymentRequest.businessName(), paymentRequest.transactionBalance());
            case PENDING -> sendGuardianApprovalMessage(user, transaction, paymentLimitType);
        }
    }

    // Guardian에게 승인 메시지 전송
    private void sendGuardianApprovalMessage(User user, Transaction transaction, PaymentLimitType paymentLimitType) {
        TransactionInfo transactionInfo = new TransactionInfo(user, transaction, paymentLimitType);
        NotificationBody notificationBody = notificationService.saveNotification(transactionInfo);

        List<String> guardianDeviceToken = user.getFamily().getUsers().stream()
                .filter(familyUser -> familyGuardianRole.contains(familyUser.getRole()))
                .map(User::getDeviceToken).toList();
        fcmSender.sendApprovalNotification(guardianDeviceToken, notificationBody);
    }

    // TODO: 시간제한에 걸려 자동으로 요청 취소
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateTimeoutTransaction() {
        log.info("TransactionService.updateTimeoutTransaction() is called.");
        List<Transaction> transactionList = transactionRepository.findAllByStatus(TransactionStatus.PENDING);

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        transactionList.stream()
        .filter(transaction -> {
           LocalDateTime transactionTime = LocalDateTime.ofInstant(transaction.getTimestamp().toInstant(), ZoneId.systemDefault());
            Long minuteElapsed = ChronoUnit.MINUTES.between(transactionTime, now);
            return minuteElapsed > TIMEOUT;
        })
        .forEach(transaction -> {
            transaction.updateTransactionStatus(TransactionStatus.REJECT);
            User user = transaction.getUser();
            switch (transaction.getTransactionType()) {
                case WITHDRAWAL -> fcmSender.sendWithdrawalFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getTransactionBalance());
                case TRANSFER -> fcmSender.sendTransferFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver(), transaction.getTransactionBalance());
                case PAYMENT ->  fcmSender.sendPaymentFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver(), transaction.getTransactionBalance());
            }
        });

        /*
         * TODO: DB에서 Status가 PENDING인 Transaction들 가져오기
         * TODO: Timestamp를 확인
         * TODO: User 가져오기
         * TODO: PaymentLimit를 가져오기
         * TODO: if(transaction.timestamp + 5분 or 10분 > now()) { // 제한시간 초과
         * TODO:    transaction.status = TransactionStatus.REJECT // 상태를 거절로 변경
         * TODO:    switch(transaction) {
         * TODO:        case WITHDRAWAL -> fcmSender.sendWithdrawalFailMessage();
         * TODO:        case TRANSFER -> fcmSender.sendTransferFailMessage();
         * TODO:        case PAYMENT -> fcmSender.sendPaymentFailMessage();
         * TODO:    }
         * TODO: }
         */
    }


    // TODO: 승인 요구치에 따른 출금 진행
    @Transactional
    @EventListener(WithdrawalApproveEvent.class)
    public void executeWithdrawalTransaction(WithdrawalApproveEvent event) {
        log.info("TransactionService.executeWithdrawalTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 출금
            accountService.updateAccountWithdrawal(user.getAccountNumber(), transaction.getTransactionBalance());
            transaction.updateTransactionStatus(TransactionStatus.APPROVE);
            fcmSender.sendWithdrawalSuccessMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getTransactionBalance());
            log.info("Success to execute withdrawal. [Transaction-ID: {}]", transaction.getId());
        }
        else if ((user.getFamily().getTotalManagerCount() - transaction.getRejectCount()) < approvalRequirement) {
            //  거부 알림
            transaction.updateTransactionStatus(TransactionStatus.REJECT);
            fcmSender.sendWithdrawalFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getTransactionBalance());
            log.info("Rejected to execute withdrawal. [Transaction-ID: {}]", transaction.getId());
        }
    }

    // TODO: 승인 요구치에 따른 이체 진행
    @Transactional
    @EventListener(TransferApproveEvent.class)
    public void executeTransferTransaction(TransferApproveEvent event) {
        log.info("TransactionService.executeTransferTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 이체
            accountService.updateAccountTransfer(transaction.getReceiver(), user.getAccountNumber(), transaction.getTransactionBalance());
            transaction.updateTransactionStatus(TransactionStatus.APPROVE);
            fcmSender.sendTransferSuccessMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver() ,transaction.getTransactionBalance());
            log.info("Success to execute transfer. [Transaction-ID: {}]", transaction.getId());
        }
        else if ((user.getFamily().getTotalManagerCount() - transaction.getRejectCount()) < approvalRequirement) {
            //  거부 알림
            transaction.updateTransactionStatus(TransactionStatus.REJECT);
            fcmSender.sendTransferFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver(), transaction.getTransactionBalance());
            log.info("Rejected to execute transfer. [Transaction-ID: {}]", transaction.getId());
        }
    }

    // TODO: 승인 요구치에 따른 결제 진행
    @Transactional
    @EventListener(PaymentApproveEvent.class)
    public void executePaymentTransaction(PaymentApproveEvent event) {
        log.info("TransactionService.executePaymentTransaction() is called.");
        // Transaction 정보 조회
        Transaction transaction = transactionRepository.findById(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction Not Found."));
        User user = transaction.getUser();
        Integer approvalRequirement = user.getFamily().getApprovalRequirement(); // 승인 요구치

        if(transaction.getApproveCount() >= approvalRequirement) { // 승인 요구치에 도달하면
            // 이체
            accountService.updateAccountWithdrawal(user.getAccountNumber(), transaction.getTransactionBalance());
            transaction.updateTransactionStatus(TransactionStatus.APPROVE);
            fcmSender.sendPaymentSuccessMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver(), transaction.getTransactionBalance());
            log.info("Success to execute payment. [Transaction-ID: {}]", transaction.getId());
        }
        else if ((user.getFamily().getTotalManagerCount() - transaction.getRejectCount()) < approvalRequirement) {
            //  거부 알림
            transaction.updateTransactionStatus(TransactionStatus.REJECT);
            fcmSender.sendPaymentFailMessage(user.getDeviceToken(), user.getAccountNumber(), transaction.getReceiver(), transaction.getTransactionBalance());
            log.info("Rejected to execute payment. [Transaction-ID: {}]", transaction.getId());
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
        else if(responseStatus.equals(ResponseStatus.REJECT)) {
            transaction.incrementRejectCount();
        }
        else {
            throw new RuntimeException("Failed to found response status.");
        }

        return transactionRepository.save(transaction);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.user().getId();
    }
}
