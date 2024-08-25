package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

public record UpdateFamilyRequest(
        Long id,
        String name,
        String description,
        Integer approvalRequirement,
        Boolean notificationStatus
) {
}
