package com.shinhan_hackathon.the_family_guardian.domain.notification.service;

import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.repository.NotificationRepository;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionInfo;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.NotificationBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationBody saveNotification(TransactionInfo transactionInfo) {

        NotificationBody notificationBody = new NotificationBody(
                transactionInfo.transaction().getId(),
                transactionInfo.transaction().getTransactionType(),
                transactionInfo.user().getAccountNumber(),
                transactionInfo.transaction().getReceiver(),
                transactionInfo.transaction().getTransactionBalance()
        );

        Notification notification = Notification.builder()
                .user(transactionInfo.user())
                .transaction(transactionInfo.transaction())
                .limitType(transactionInfo.limitType())
                .title("결제 승인 요청")
                .body(notificationBody.toString())
                .requiresResponse(true)
                .build();

        notificationRepository.save(notification);
        return notificationBody;
    }
}
