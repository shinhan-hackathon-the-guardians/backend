package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

public record FamilyUserRoleResponse(
        Long userId,
        Role role
) {
}
