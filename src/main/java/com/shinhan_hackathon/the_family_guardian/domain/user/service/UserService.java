package com.shinhan_hackathon.the_family_guardian.domain.user.service;

import com.shinhan_hackathon.the_family_guardian.domain.user.dto.SignupRequest;
import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public void createUser(SignupRequest signupRequest) {

        validateSignupRequest(signupRequest);

        // TODO 1원송금 검증
        userRepository.save(signupRequest.toUserEntity());

    }

    public boolean validateSignupRequest(SignupRequest signupRequest) {
        return true;
    }

    public String validUsername(String username) {
        // TODO 검증로직 추가
        return username;
    }

    public String validPassword(String password, String passwordCheck) {
        if (password == null || passwordCheck == null) {
            throw new IllegalArgumentException("pw가 null입니다.");
        }

        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("pw가 일치하지 않습니다.");
        }

        return password;
    }
}
