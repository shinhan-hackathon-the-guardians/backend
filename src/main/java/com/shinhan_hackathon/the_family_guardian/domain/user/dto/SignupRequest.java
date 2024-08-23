package com.shinhan_hackathon.the_family_guardian.domain.user.dto;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.Gender;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;

import java.time.LocalDate;

public record SignupRequest(
        String username,
        String password,
        String passwordCheck,
        String name,
        Gender gender,
        LocalDate birthDate,
        String phoneNumber,
        String accountNumber,
        String csrfToken
) {

    public User toUserEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .gender(gender)
                .birthDate(birthDate)
                .phone(phoneNumber)
                .accountNumber(accountNumber)
                .build();
    }
}
