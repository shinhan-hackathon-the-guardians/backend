package com.shinhan_hackathon.the_family_guardian.domain.approval.dto;

import com.shinhan_hackathon.the_family_guardian.domain.approval.entity.AcceptStatus;

public record ApprovalReplyResponse(
        AcceptStatus acceptStatus,
        FamilyInfo familyInfo
) {
    public record FamilyInfo(
            Long id,
            String name,
            String description
    ) {
    }
}
