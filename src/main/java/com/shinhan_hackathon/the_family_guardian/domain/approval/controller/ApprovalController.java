package com.shinhan_hackathon.the_family_guardian.domain.approval.controller;

import com.shinhan_hackathon.the_family_guardian.domain.approval.dto.ApprovalInfoResponse;
import com.shinhan_hackathon.the_family_guardian.domain.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping
    public ResponseEntity<ApprovalInfoResponse> searchApproval() {
        return ResponseEntity.ok(approvalService.getApproval());
    }
}
