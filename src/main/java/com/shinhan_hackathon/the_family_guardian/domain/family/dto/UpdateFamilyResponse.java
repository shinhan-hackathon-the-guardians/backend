package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

public record UpdateFamilyResponse(
        Long id,
        String name,
        String description,
        Integer approvalRequirement
) {
}
