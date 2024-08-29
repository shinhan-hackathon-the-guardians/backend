package com.shinhan_hackathon.the_family_guardian.domain.notification.controller;

import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationHistory;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.PendingNotificationResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.service.NotificationService;
import com.shinhan_hackathon.the_family_guardian.domain.transaction.dto.NotificationBody;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;
import com.shinhan_hackathon.the_family_guardian.global.auth.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthUtil authUtil;

    @PostMapping("/reply")
    public ResponseEntity<NotificationReplyResponse> postGuardianReply(@RequestBody NotificationReplyRequest notificationReplyRequest) {
        NotificationReplyResponse notificationReplyResponse = notificationService.reflectReply(
                notificationReplyRequest.notificationId(),
                notificationReplyRequest.isApprove()
        );

        return ResponseEntity.ok(notificationReplyResponse);
    }

    @GetMapping("/unanswered}")
    public ResponseEntity<PendingNotificationResponse> getPendingNotification() {
        authUtil.checkAuthority(Role.MANAGER, Role.OWNER);
        PendingNotificationResponse pendingNotification = notificationService.findUnansweredNotification();
        return ResponseEntity.ok(pendingNotification);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<NotificationHistory>> getUserNotification(@PathVariable(value = "user_id") Long userId) {
        List<NotificationHistory> notificationByUserId = notificationService.findNotificationByUserId(userId);
        return ResponseEntity.ok(notificationByUserId);
    }

    @GetMapping("/{notification_id}")
    public ResponseEntity<NotificationBody> getNotification(@PathVariable(value = "notification_id") Long notificationId) {
        authUtil.checkAuthority(Role.MANAGER, Role.OWNER);

        NotificationBody notificationBody = notificationService.findNotification(notificationId);
        return ResponseEntity.ok(notificationBody);
    }
}
