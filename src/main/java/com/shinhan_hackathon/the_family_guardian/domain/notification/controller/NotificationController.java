package com.shinhan_hackathon.the_family_guardian.domain.notification.controller;

import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.notification.dto.NotificationReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
