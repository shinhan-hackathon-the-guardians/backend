package com.shinhan_hackathon.the_family_guardian.domain.notification.service;

import com.shinhan_hackathon.the_family_guardian.domain.family.entity.Family;
import com.shinhan_hackathon.the_family_guardian.domain.family.service.FamilyService;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationHistory;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.PendingNotification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.PendingNotificationResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.Notification;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.NotificationResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.notification.entity.ResponseStatus;
import com.shinhan_hackathon.the_family_guardian.domain.notification.repository.NotificationRepository;
import com.shinhan_hackathon.the_family_guardian.domain.notification.repository.NotificationResponseStatusRepository;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.NotificationBody;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.TransactionInfo;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.Transaction;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.entity.TransactionStatus;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.repository.TransactionRepository;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import com.shinhan_hackathon.the_family_guardian.global.event.EventPublisher;

import java.util.*;

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
    private final FamilyService familyService;
    private final EventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final NotificationResponseStatusRepository notificationResponseStatusRepository;
    private final AuthUtil authUtil;

    @Transactional
    public Notification saveNotification(TransactionInfo transactionInfo) {

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

        return notificationRepository.save(notification);
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
        TransactionStatus transactionStatus = null;
        ResponseStatus responseStatus = null;
        int approveCount = notification.getTransaction().getApproveCount();
        int rejectCount = notification.getTransaction().getRejectCount();

        if (isApprove) {
            notification.getTransaction().incrementApproveCount();
            eventPublisher.publishTransactionApproveEvent(eventTrackingId, notificationId);
            transactionStatus = TransactionStatus.APPROVE;
            responseStatus = ResponseStatus.APPROVE;
        } else {
            notification.getTransaction().incrementRejectCount();
            eventPublisher.publishTransactionRejectEvent(eventTrackingId, notificationId);
            transactionStatus = TransactionStatus.REJECT;
            responseStatus = ResponseStatus.REJECT;
        }

        User guardian = authUtil.getUserPrincipal().user();
        NotificationResponseStatus notificationResponseStatus = notificationResponseStatusRepository.findByGuardianAndNotification(guardian, notification)
                .orElseThrow(() -> new RuntimeException("알림 응답 객체가 없습니다."));
        notificationResponseStatus.updateResponseStatus(responseStatus);

        return new NotificationReplyResponse(
                notification.getTransaction().getId(),
                transactionStatus,
                approveCount,
                rejectCount
        );
    }

    public PendingNotificationResponse findAllUnansweredNotification() {
        User guardian = authUtil.getUserPrincipal().user();
        List<NotificationResponseStatus> responseStatusList = notificationResponseStatusRepository.findAllByGuardianAndResponseStatus(guardian, ResponseStatus.NONE);
        Map<Long, List<Notification>> unansweredNotificationMap = new HashMap<>();

        for (NotificationResponseStatus responseStatus : responseStatusList) {
            Notification notification = responseStatus.getNotification();
            User user = notification.getUser();
            List<Notification> notifications = unansweredNotificationMap.putIfAbsent(user.getId(), new ArrayList<>(List.of(notification)));
            if (notifications != null) {
                notifications.add(notification);
            }
        }

        List<PendingNotification> pendingNotifications = new ArrayList<>();
        for (Map.Entry<Long, List<Notification>> notificationList : unansweredNotificationMap.entrySet()) {
            Long userId = notificationList.getKey();
            List<Notification> notifications = notificationList.getValue();

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                pendingNotifications.add(new PendingNotification(user.getId(), user.getName(), notifications.size()));
            }
        }

        return new PendingNotificationResponse(pendingNotifications);
    }

    @Deprecated
    public PendingNotificationResponse findUnansweredNotification() {
        User guardian = authUtil.getUserPrincipal().user();
        List<NotificationResponseStatus> responseStatusList = notificationResponseStatusRepository.findAllByGuardianAndResponseStatus(guardian, ResponseStatus.NONE);
        Map<Long, List<Notification>> unansweredNotificationMap = new HashMap<>();

        for (NotificationResponseStatus responseStatus : responseStatusList) {
            Notification notification = responseStatus.getNotification();
            User user = notification.getUser();
            List<Notification> notifications = unansweredNotificationMap.putIfAbsent(user.getId(), new ArrayList<>(List.of(notification)));
            if (notifications != null) {
                notifications.add(notification);
            }
        }

        List<PendingNotification> pendingNotifications = new ArrayList<>();
        for (Map.Entry<Long, List<Notification>> notificationList : unansweredNotificationMap.entrySet()) {
            Long userId = notificationList.getKey();
            List<Notification> notifications = notificationList.getValue();

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                pendingNotifications.add(new PendingNotification(user.getId(), user.getName(), notifications.size()));
            }
        }

        return new PendingNotificationResponse(pendingNotifications);
    }

    @Deprecated
    public PendingNotificationResponse getPendingNotification(Long groupId) {
        Family family = familyService.findByGroupId(groupId);
        List<PendingNotification> list = new ArrayList<>();

        for(User user : family.getUsers()){
            List<Notification> notificationList = notificationRepository.findByUserId(user.getId());
            int notificationCount = 0;
            for(Notification notification : notificationList) {
                Transaction transaction = notification.getTransaction();
                if(transaction.getStatus().equals(TransactionStatus.PENDING)) {
                    notificationCount++;
                }
            }

            if (notificationCount > 0) {
                list.add(new PendingNotification(user.getId(), user.getName(), notificationCount));
            }
        }
        PendingNotificationResponse response = new PendingNotificationResponse(list);
        return response;
    }

    public List<NotificationHistory> findNotificationByUserId(Long userId) {
        User user = userRepository.getReferenceById(userId);
        List<Notification> notificationList = notificationRepository.findAllByUser(user);


        return notificationList.stream().map(notification -> {
            Transaction transaction = notification.getTransaction();
            return new NotificationHistory(
                    notification.getId(),
                    transaction.getTransactionType(),
                    transaction.getTransactionBalance()
            );
        }).toList();
    }

    public NotificationBody findNotification(Long notificationId) {
        Long userId = Long.valueOf(authUtil.getUserPrincipal().getUsername());
        User guardian = userRepository.getReferenceById(userId);
        Notification notification = notificationRepository.getReferenceById(notificationId);
        NotificationResponseStatus responseStatus = notificationResponseStatusRepository.findByGuardianAndNotification(guardian, notification)
                .orElseThrow(() -> new RuntimeException("없는 알림입니다."));

        if (!responseStatus.getResponseStatus().equals(ResponseStatus.NONE)) {
            throw new RuntimeException("이미 응답한 알림입니다.");
        }

        Transaction transaction = notification.getTransaction();
        return new NotificationBody(
                notification.getId(),
                transaction.getTransactionType(),
                transaction.getUser().getAccountNumber(),
                transaction.getReceiver(),
                transaction.getTransactionBalance()
        );
    }
}
