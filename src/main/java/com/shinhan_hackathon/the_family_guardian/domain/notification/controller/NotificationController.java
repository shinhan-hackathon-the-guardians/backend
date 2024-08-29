package com.shinhan_hackathon.the_family_guardian.domain.notification.controller;

import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.PendingNotificationResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/reply")
    public ResponseEntity<NotificationReplyResponse> postGuardianReply(@RequestBody NotificationReplyRequest notificationReplyRequest) {
        NotificationReplyResponse notificationReplyResponse = notificationService.reflectReply(
                notificationReplyRequest.notificationId(),
                notificationReplyRequest.isApprove()
        );

        return ResponseEntity.ok(notificationReplyResponse);
    }

    @GetMapping("/group/{group_id}")
    public ResponseEntity<PendingNotificationResponse> getPendingNotification(@PathVariable Long group_id) {
        return ResponseEntity.ok(notificationService.getPendingNotification(group_id));
    }
}
