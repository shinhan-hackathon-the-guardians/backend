package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

public record AddFamilyMemberRequest(
        String name,
        String phoneNumber,
        String relationship,
        Role role
) {
}
