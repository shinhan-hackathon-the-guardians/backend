package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

public record CreateFamilyRequest(
        String name,
        String description,
        Integer approvalRequirement
) {
}
