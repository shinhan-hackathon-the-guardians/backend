package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

import java.util.List;

public record FamilyAndUserResponse(
        String name,
        String description,
        Integer approvalRequest,
        List<FamilyUser> userList
) {

    public record FamilyUser (
            Long id,
            String name,
            String birthdate,
            Level level,
            Role role,
            String relationship
    ) {
    }
}
