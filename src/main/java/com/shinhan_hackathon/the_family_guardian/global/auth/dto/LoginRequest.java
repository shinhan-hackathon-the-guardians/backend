package com.shinhan_hackathon.the_family_guardian.global.auth.dto;

public record LoginRequest (
        String email,
        String password
) {
}
