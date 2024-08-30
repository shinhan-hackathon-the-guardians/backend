package com.shinhan_hackathon.the_family_guardian.domain.user.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

public record SignupResponse(
        Long userId,
        String userName,
        Level level,
        Role role,
        Long familyId,
        String familyName
) {
}
