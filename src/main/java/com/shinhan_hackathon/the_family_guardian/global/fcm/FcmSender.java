package com.shinhan_hackathon.the_family_guardian.global.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.NotificationBody;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmSender implements MessageSender {
    private final ObjectMapper jacksonObjectMapper;
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

    public void sendApprovalNotification(List<String> guardianDeviceTokenList, String body) {
        for (String deviceToken : guardianDeviceTokenList) {
            sendMessage(deviceToken, "승인요청", body);
        }
    }

    // public: FCM 접근 메서드
    public void sendPaymentSuccessMessage(String deviceToken, String senderAccountNumber, String receiverName, long transactionBalance) {
        sendPaymentResultMessage(deviceToken, senderAccountNumber, receiverName, transactionBalance, true);
    }
    public void sendPaymentFailMessage(String deviceToken, String senderAccountNumber, String receiverName, long transactionBalance) {
        sendPaymentResultMessage(deviceToken, senderAccountNumber, receiverName, transactionBalance, false);
    }
    public void sendTransferSuccessMessage(String deviceToken, String senderAccountNumber, String receiverAccountNumber, long transactionBalance) {
        sendTransferResultMessage(deviceToken, senderAccountNumber, receiverAccountNumber, transactionBalance, true);
    }
    public void sendTransferFailMessage(String deviceToken, String senderAccountNumber, String receiverAccountNumber, long transactionBalance) {
        sendTransferResultMessage(deviceToken, senderAccountNumber, receiverAccountNumber, transactionBalance, false);
    }
    public void sendWithdrawalSuccessMessage(String deviceToken, String userAccountNumber, long transactionBalance) {
        sendWithdrawalResultMessage(deviceToken, userAccountNumber, transactionBalance, true);
    }
    public void sendWithdrawalFailMessage(String deviceToken, String userAccountNumber, long transactionBalance) {
        sendWithdrawalResultMessage(deviceToken, userAccountNumber, transactionBalance, false);
    }


    // private: FCM 전송 메서드 호출
    private void sendPaymentResultMessage(String deviceToken, String senderAccountNumber, String receiver, long transactionBalance, boolean isSuccess) {
        sendTransactionResultMessage(
                deviceToken,
                TransactionType.PAYMENT,
                senderAccountNumber,
                receiver,
                transactionBalance,
                "결제",
                isSuccess
        );
    }
    private void sendTransferResultMessage(String deviceToken, String senderAccountNumber, String receiverAccountNumber, long transactionBalance, boolean isSuccess) {
        sendTransactionResultMessage(
                deviceToken,
                TransactionType.TRANSFER,
                senderAccountNumber,
                receiverAccountNumber,
                transactionBalance,
                "이체",
                isSuccess
        );
    }
    private void sendWithdrawalResultMessage(String deviceToken, String userAccountNumber, long transactionBalance, boolean isSuccess) {
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

    // FCM 전송 메서드
    private void sendTransactionResultMessage(String deviceToken, TransactionType txType, String senderAccount, String receiver, long transactionBalance, String txName, boolean isSuccess) {
        NotificationBody notificationBody = new NotificationBody(
                0L,
                txType,
                senderAccount,
                receiver,
                transactionBalance
        );

        try {
            String bodyStr = jacksonObjectMapper.writeValueAsString(notificationBody);
            sendMessage(
                    deviceToken,
                    txName + (isSuccess ? " 성공" : " 실패"),
                    bodyStr
            );
        } catch (JsonProcessingException e) {
            log.info("body 파싱 실패");
            throw new RuntimeException(e);
        }
    }
}
