package com.shinhan_hackathon.the_family_guardian.domain.approval.controller;

import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalReplyRequest;
import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalInfoResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalReplyResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping
    public ResponseEntity<List<ApprovalInfoResponse>> searchApproval() {
        return ResponseEntity.ok(approvalService.getApproval());
    }

    @PostMapping("/reply")
    public ResponseEntity<ApprovalReplyResponse> acceptApproval(@RequestBody ApprovalReplyRequest approvalReplyRequest) {
        ApprovalReplyResponse approvalReplyResponse = approvalService.acceptApproval(
                approvalReplyRequest.approvalId(),
                approvalReplyRequest.acceptStatus()
        );

        return ResponseEntity.ok(approvalReplyResponse);
    }
}
