package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Level;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Role;

import java.time.LocalDate;
import java.util.List;

public record FamilyInfoResponse (
        String name,
        String description,
        Integer approvalRequest,
        List<FamilyUser> userList
) {

    public record FamilyUser (
            Long id,
            String name,
            LocalDate birthDate,
            Level level,
            Role role,
            String relationship
    ) {
    }
}
