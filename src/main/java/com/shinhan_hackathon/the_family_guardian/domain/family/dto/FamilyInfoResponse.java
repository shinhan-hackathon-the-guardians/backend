package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import java.time.LocalDate;

public record FamilyInfoResponse (
        String name,
        String description,
        Integer approvalRequest,
        LocalDate createdAt
) {
}
