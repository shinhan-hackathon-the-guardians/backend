package com.shinhan_hackathon.the_family_guardian.domain.user.dto;

public record AccountAuthSendRequest(
        String accountNumber,
        String deviceToken
) {
}
