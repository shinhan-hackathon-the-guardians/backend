package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

public record AddFamilyMemberResponse(
        Long userId,
        String name,
        String phoneNumber,
        Long approvalId
) {
}
