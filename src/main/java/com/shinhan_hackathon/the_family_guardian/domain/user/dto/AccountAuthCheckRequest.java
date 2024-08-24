package com.shinhan_hackathon.the_family_guardian.domain.user.dto;

public record AccountAuthCheckRequest(
        String accountNumber,
        String authCode,
        String csrfToken
) {
}
