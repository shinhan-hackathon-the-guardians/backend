package com.shinhan_hackathon.the_family_guardian.domain.approval.dto;

public record ApprovalReplyRequest(
        Long approvalId,
        Boolean acceptStatus
) {
}
