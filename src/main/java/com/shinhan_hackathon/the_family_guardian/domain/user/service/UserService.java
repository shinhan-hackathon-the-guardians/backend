package com.shinhan_hackathon.the_family_guardian.domain.user.service;

import com.shinhan_hackathon.the_family_guardian.domain.user.entity.User;
import com.shinhan_hackathon.the_family_guardian.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public String getAccountNumber(Long userId) {
        log.info("UserService.getAccountNumber() is called.");
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Failed to found user."));

        return user.getAccountNumber();
    }
}
