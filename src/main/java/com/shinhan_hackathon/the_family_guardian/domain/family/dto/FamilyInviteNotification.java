package com.shinhan_hackathon.the_family_guardian.domain.family.dto;

public record FamilyInviteNotification (
        Long id,
        String name,
        String description,
        Long ownerId,
        String ownerName
) {
}
