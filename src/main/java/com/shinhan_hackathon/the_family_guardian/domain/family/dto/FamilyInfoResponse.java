package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

public record FamilyInfoResponse (
        String name,
        String description,
        Integer approvalRequest,
        String createdAt
) {
}
