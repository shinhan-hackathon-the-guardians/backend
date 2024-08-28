package com.shinhan_hackathon.the_family_guardian.domain.notification.service;

import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.ResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.notification.repository.NotificationRepository;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.NotificationBody;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionInfo;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionStatus;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.service.TransactionService;
import com.shinhan_hackathon.the_family_guardian.global.event.EventPublisher;
import java.util.UUID;
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
    private final EventPublisher eventPublisher;
    private final TransactionService transactionService;

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

    @Transactional
    public NotificationReplyResponse reflectReply(Long notificationId, Boolean isApprove) {
        /**
         * 가디언의 응답을 반영
         * isApprove == true -> 찬성 1증가
         * isApprove == false -> 반대 1증가
         */
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 알림입니다."));

        String eventTrackingId = UUID.randomUUID().toString();
        TransactionStatus transactionStatus = TransactionStatus.PENDING;
        int approveCount = notification.getTransaction().getApproveCount();
        int rejectCount = notification.getTransaction().getRejectCount();

        ResponseStatus responseStatus = isApprove ? ResponseStatus.APPROVE : ResponseStatus.REJECT;
        transactionService.updateTransactionApproveCount(notification.getTransaction().getId(), responseStatus);

        if (isApprove) {
            eventPublisher.publishTransactionApproveEvent(eventTrackingId, notificationId);
            transactionStatus = TransactionStatus.APPROVE;
        } else {
            eventPublisher.publishTransactionRejectEvent(eventTrackingId, notificationId);
            transactionStatus = TransactionStatus.REJECT;
        }

        return new NotificationReplyResponse(
                notification.getTransaction().getId(),
                transactionStatus,
                approveCount,
                rejectCount
        );
    }
}
