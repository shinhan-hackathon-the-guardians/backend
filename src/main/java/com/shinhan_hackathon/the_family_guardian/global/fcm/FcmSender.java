package com.shinhan_hackathon.the_family_guardian.global.fcm;

import com.google.api.core.ApiFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionResultInfo;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmSender implements MessageSender {
    @Value("${firebase.app-name}")
    private String firebaseAppName;

    private final Executor threadPoolTaskExecutor;

    @Override
    public void sendMessage(String deviceToken, String title, String body) {
        log.info("token: {}, title: {}, body: {}", deviceToken, title, body);

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        ApiFuture<String> apiFuture = FirebaseMessaging.getInstance(FirebaseApp.getInstance(firebaseAppName)).sendAsync(message);
        apiFuture.addListener(task(apiFuture), threadPoolTaskExecutor);
    }

    private Runnable task(ApiFuture<String> apiFuture) {
        return () -> {
            try {
                String response = apiFuture.get();
                log.info("Successfully sent message: {}", response);
                log.info("Current Thread: {}", Thread.currentThread().getName());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public void sendWithdrawalSuccessMessage(String deviceToken, String userAccountNumber, long transactionBalance) {
        sendWithdrawalResultMessage(deviceToken, userAccountNumber, transactionBalance, true);
    }
    public void sendWithdrawalFailMessage(String deviceToken, String userAccountNumber, long transactionBalance) {
        sendWithdrawalResultMessage(deviceToken, userAccountNumber, transactionBalance, true);
    }
    public void sendWithdrawalResultMessage(String deviceToken, String userAccountNumber, long transactionBalance, boolean isSuccess) {
        sendTransactionResultMessage(
                deviceToken,
                TransactionType.WITHDRAWAL,
                userAccountNumber,
                null,
                transactionBalance,
                "출금",
                isSuccess
        );
    }
    private void sendTransactionResultMessage(String deviceToken, TransactionType txType, String senderAccount, String receiverAccount, long transactionBalance, String txName, boolean isSuccess) {
        TransactionResultInfo transactionResultInfo = new TransactionResultInfo(
                txType,
                senderAccount,
                receiverAccount,
                transactionBalance
        );

        sendMessage(
                deviceToken,
                txName + (isSuccess ? " 성공" : " 실패"),
                transactionResultInfo.toString()
        );
    }
}
